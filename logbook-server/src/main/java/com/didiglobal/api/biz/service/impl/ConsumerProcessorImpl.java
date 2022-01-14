package com.didiglobal.api.biz.service.impl;

import com.didiglobal.api.biz.service.ConsumerProcessor;
import com.didiglobal.bizconfig.manager.consumerule.MappingConsumeRuleManager;
import com.didiglobal.bizconfig.manager.producer.ProducerConfigManager;
import com.didiglobal.common.LogbookError;
import com.didiglobal.common.LogbookException;
import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.param.RetryConsumeParam;
import com.didiglobal.common.pojo.ConsumeMessageContext;
import com.didiglobal.common.pojo.LogbookConstants;
import com.didiglobal.common.pojo.dto.EntityEventDto;
import com.didiglobal.common.pojo.vo.ConsumeVo;
import com.didiglobal.common.pojo.vo.FilterVo;
import com.didiglobal.common.pojo.vo.ProduceVo;
import com.didiglobal.mybatis.entity.EntityEventDispatchRecord;
import com.didiglobal.mybatis.entity.MappingConsumeRule;
import com.didiglobal.mediator.filter.EntityEventFilterFactory;
import com.didiglobal.mediator.filter.EntityEventFilterMediator;
import com.didiglobal.mediator.storage.EntityEventDispatchRecordStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author liyanling
 * @date 2021/11/18 8:01 下午
 */
@Component
@Slf4j
public class ConsumerProcessorImpl implements ConsumerProcessor<Object> {

    @Autowired
    EntityEventDispatchRecordStorage storage;

    @Autowired
    MappingConsumeRuleManager manager;

    @Autowired
    EntityEventFilterFactory entityEventFilterFactory;

    @Autowired
    ObjectMapper objectMapper;

    // 生产者 manager
    @Autowired
    ProducerConfigManager producerConfigManager;


    /**
     * 消费业务逻辑
     *
     * @param consumeMessageContext
     * @param params
     * @return
     */
    @Override
    public ConsumeVo<Object> consume(ConsumeMessageContext consumeMessageContext, Object... params) {
        try {
            return doConsume(consumeMessageContext);
        } catch (LogbookException le) {
            // LogbookException 一律处理为不可重试
            log.error("doConsume caught exception,le=", le);
            return ConsumeVo.builder().success(false).needRetry(false).result(le.getErrmsg()).build();
        } catch (Exception e) {
            // Exception 一律处理为可重试
            log.error("doConsume caught exception,e=", e);
            return ConsumeVo.builder().success(false).needRetry(true).result(e.getMessage()).build();
        }
    }

    /**
     * 重试消费（重新推送到消息队列）
     *
     * @param consumeMessageContext
     * @param params
     */
    @Override
    public void retryConsume(ConsumeMessageContext consumeMessageContext, Object... params) {
        try {
            producerConfigManager
                    .produce(consumeMessageContext.getTopicName(), consumeMessageContext.getConsumeMessageBody());
            log.info("retryConsume reproduce message to topic success,topicName={},messageBody={}",
                    consumeMessageContext.getTopicName(), consumeMessageContext.getConsumeMessageBody());
        } catch (Exception e) {
            log.error("retryConsume reproduce to {} caught exception,e=",
                    consumeMessageContext.getTopicName(), e);
        }
    }

    /**
     * 重试消费
     * @param callerInfo
     * @param param
     * @return
     */
    @Override
    public ConsumeVo<Object> consumeRetry(CallerInfo callerInfo, RetryConsumeParam param) {
        paramCheck(callerInfo,param);

        // 1. 查询记录
        String consumeGlobalEventId = param.getConsumeGlobalEventId();
        EntityEventDispatchRecord recordInDb = getEntityEventDispatchRecordForConsumeRetry(consumeGlobalEventId);

        // 2. 执行 filter，得到要分发的消息体
        EntityEventDto entityEventDto = getEntityEventDto(recordInDb);
        MappingConsumeRule rule = manager.getConsumeRule(recordInDb.getConsumePluginName());
        String filteredMessage = executeFilter(entityEventDto, rule);

        // 3. 执行 dispatch
        doDispatch(rule.getDispatchDestination(), filteredMessage);

        // 4. update 数据库
        updateEntityEventDispatchRecordSuccess(recordInDb);

        // 5. 返回结果
        return ConsumeVo.builder().success(true).result(filteredMessage).build();
    }



    /**
     * consume 的核心逻辑
     *
     * @param consumeMessageContext
     * @return
     */
    private ConsumeVo<Object> doConsume(ConsumeMessageContext consumeMessageContext) {
        // 1. insert 到数据库
        EntityEventDispatchRecord record = buildRecord(consumeMessageContext);
        //TODO 如果失败了怎么重试
        if (storage.insert(record) == 0) {
            return conflictConsumeVo(record);
        }

        // 2. 执行 filter，得到要分发的消息体
        MappingConsumeRule rule = manager.getConsumeRule(record.getConsumePluginName());
        String filteredMessage = executeFilter(consumeMessageContext.getEntityEventDto(), rule);

        // 3. 执行 dispatch
        doDispatch(rule.getDispatchDestination(), filteredMessage);

        // 4. update 数据库
        updateEntityEventDispatchRecordSuccess(record);

        // 5. 返回结果
        return ConsumeVo.builder().success(true).result(filteredMessage).build();
    }


    /**
     * 只要插入失败，就直接返回结果
     *
     * @param record
     * @return
     */
    private ConsumeVo<Object> conflictConsumeVo(EntityEventDispatchRecord record) {
        EntityEventDispatchRecord recordInDb = storage.queryByConsumeGlobalEventId(record.getConsumeGlobalEventId());

        // 插入失败、没查到 => 失败、需重试
        if (recordInDb == null) {
            log.warn("conflictConsumeVo insert failed and query failed,record={}", record);
            return ConsumeVo.builder().success(false).needRetry(true).result(record).build();
        }

        // 插入失败、已作废 => 失败、不重试
        if(recordInDb.getStatus() == LogbookConstants.ENTITY_EVENT_PROCESS_ABANDONED){
            log.warn("conflictConsumeVo insert failed and query result is abandoned,recordInDb={}", recordInDb);
            return ConsumeVo.builder().success(false).needRetry(false).result(recordInDb).build();
        }

        // 插入失败、已成功 => 成功、不重试
        // 插入失败、处理中 => 成功、不重试【直接在在这里做重试可能会重复发消息，提供单独的接口做重试、或作废】
        log.warn("conflictConsumeVo insert failed and query result is processing/succeed,recordInDb={}", recordInDb);
        return ConsumeVo.builder().success(true).needRetry(false).result(recordInDb).build();
    }


    // 执行 filter，得到要发送的消息体
    private String executeFilter(EntityEventDto entityEventDto, MappingConsumeRule rule) {
        if (null == rule) {
            throw new LogbookException(LogbookError.NOT_FOUND_FILTER_RULE);
        }

        EntityEventFilterMediator mediator =
                entityEventFilterFactory.getEntityEventFilterMediator(rule.getFilterType());
        FilterVo<Object> filterVo = mediator.filter(entityEventDto, rule);
        if (null == filterVo || !filterVo.isSuccess() || null == filterVo.getResult()) {
            log.error("executeFilter failed,filterVo={}", filterVo);
            throw new LogbookException(LogbookError.FAILED_FILTER);
        }

        try {
            return objectMapper.writeValueAsString(filterVo.getResult());
        } catch (JsonProcessingException e) {
            log.error("executeFilter failed,filterVo={},e=", filterVo, e);
            throw new LogbookException(LogbookError.FAILED_SERIALIZE_TO_STRING);
        }
    }

    // 更新状态为成功
    private void updateEntityEventDispatchRecordSuccess(EntityEventDispatchRecord record) {
        int oldStatus = record.getStatus();
        record.setStatus(LogbookConstants.ENTITY_EVENT_PROCESS_COMPLETED);
        record.setSuccessTime(new Date());
        storage.update(record,oldStatus);
    }


    // 执行 dispatch
    private void doDispatch(String destination, String messageBody) {
        ProduceVo<Object> produceVo = producerConfigManager.produce(destination, messageBody);
        if (null == produceVo || !produceVo.isSuccess()) {
            log.error("doDispatch failed,produceVo={}", produceVo);
            throw new LogbookException(LogbookError.FAILED_DISPATCH);
        }
    }


    private EntityEventDispatchRecord getEntityEventDispatchRecordForConsumeRetry(String consumeGlobalEventId) {
        EntityEventDispatchRecord recordInDb = storage.queryByConsumeGlobalEventId(consumeGlobalEventId);
        if(recordInDb==null){
            log.error("getEntityEventDispatchRecordForConsumeRetry failed,no record found");
            throw new LogbookException(LogbookError.NOT_FOUND_DISPATCH_RECORD);
        }
        if(recordInDb.getStatus()==LogbookConstants.BIZ_EVENT_PROCESS_COMPLETED){
            log.error("getEntityEventDispatchRecordForConsumeRetry success,but consume already completed");
            throw new LogbookException(LogbookError.SUCCEED);
        }
        if(recordInDb.getStatus()==LogbookConstants.BIZ_EVENT_PROCESS_ABANDONED){
            log.error("getEntityEventDispatchRecordForConsumeRetry success,but consume already abandoned");
            throw new LogbookException(LogbookError.ALREADY_CONSUME_ABANDON);
        }
        return recordInDb;
    }



    private EntityEventDispatchRecord buildRecord(ConsumeMessageContext consumeMessageContext) {
        EntityEventDispatchRecord record = new EntityEventDispatchRecord();
        record.setConsumeGlobalEventId(consumeMessageContext.getConsumeGlobalEventId());
        record.setConsumePluginName(consumeMessageContext.getConsumeGroupName());
        record.setConsumeMessageContext(getContextString(consumeMessageContext));
        record.setStatus(LogbookConstants.ENTITY_EVENT_PROCESS_WAIT_START);
        record.setTraceId(consumeMessageContext.getConsumeTraceId());
        record.setSpanId(consumeMessageContext.getConsumeSpanId());
        record.setCreateTime(new Date());
        return record;
    }

    private String getContextString(ConsumeMessageContext consumeMessageContext) {
        try {
            return objectMapper.writeValueAsString(consumeMessageContext);
        } catch (JsonProcessingException e) {
            log.error("getContextString failed,e=", e);
            throw new LogbookException(LogbookError.FAILED_SERIALIZE_TO_STRING);
        }
    }

    private EntityEventDto getEntityEventDto(EntityEventDispatchRecord recordInDb) {
        try {
            ConsumeMessageContext consumeMessageContext =
                    objectMapper.readValue(recordInDb.getConsumeMessageContext(),ConsumeMessageContext.class);
            return consumeMessageContext.getEntityEventDto();
        } catch (IOException e) {
            log.error("getEntityEventDto caught exception,e=",e);
            throw new LogbookException(LogbookError.FAILED_DESERIALIZE_FROM_STRING);
        }
    }

    private void paramCheck(CallerInfo callerInfo, RetryConsumeParam param) {
        if(callerInfo == null || param==null || StringUtils.isEmpty(param.getConsumeGlobalEventId())){
            log.warn("paramCheck failed,callerInfo={},param={}",callerInfo,param);
            throw new LogbookException(LogbookError.PARAM_INVALID);
        }
    }

}
