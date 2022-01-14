package com.didiglobal.api.biz.service.impl;


import com.didiglobal.api.biz.service.BizEventService;
import com.didiglobal.bizconfig.manager.entityeventtype.EntityEventTypeManager;
import com.didiglobal.bizconfig.manager.entitytype.MappingEntityProducerConfigManager;
import com.didiglobal.bizconfig.manager.permit.PermitRuleConfigManager;
import com.didiglobal.bizconfig.manager.producer.ProducerConfigManager;
import com.didiglobal.bizconfig.manager.translate.TranslateRuleConfigManager;
import com.didiglobal.common.LogbookError;
import com.didiglobal.common.LogbookException;
import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.param.BizEventParam;
import com.didiglobal.common.param.RetryReportParam;
import com.didiglobal.common.pojo.LogbookConstants;
import com.didiglobal.common.pojo.dto.EntityEventDto;
import com.didiglobal.common.pojo.vo.*;
import com.didiglobal.common.trace.TraceUtil;
import com.didiglobal.common.translate.TranslateInput;
import com.didiglobal.common.translate.TranslateOutput;
import com.didiglobal.mybatis.entity.*;
import com.didiglobal.mediator.permit.PermitMediator;
import com.didiglobal.mediator.storage.BizEventRecordStorage;
import com.didiglobal.mediator.permit.PermitMediatorFactory;
import com.didiglobal.mediator.translate.TranslateMediator;
import com.didiglobal.mediator.translate.TranslateMediatorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liyanling
 * @date 2021/11/15 9:09 下午
 */

@Service
@Slf4j
public class BizEventServiceImpl implements BizEventService {

    // 准入配置 manager
    @Autowired
    PermitRuleConfigManager permitRuleConfigManager;

    // PermitMediator 工厂
    @Autowired
    PermitMediatorFactory permitMediatorFactory;

    // 翻译配置 manager
    @Autowired
    TranslateRuleConfigManager translateRuleConfigManager;

    // TranslateMediator 工厂
    @Autowired
    TranslateMediatorFactory translateMediatorFactory;

    // 生产者配置 manager
    @Autowired
    MappingEntityProducerConfigManager mappingEntityProducerConfigManager;

    // 生产者 manager
    @Autowired
    ProducerConfigManager producerConfigManager;

    // biz_event 表
    @Autowired
    BizEventRecordStorage bizEventRecordStorage;


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityEventTypeManager entityEventTypeManager;


    /**
     * 业务事件 准入
     *
     * @param callerInfo
     * @param bizEventParam
     * @return
     */
    @Override
    public PermitVo<Object> permit(CallerInfo callerInfo, BizEventParam bizEventParam) {
        // 1. 参数检查
        permitParamCheck(callerInfo, bizEventParam);

        // 2. 获取准入配置
        PermitRuleConfig permitRuleConfig = permitRuleConfigManager
                .getPermitRuleConfig(bizEventParam.getBizEventType());
        if (null == permitRuleConfig) {
            log.error("no permitRuleConfig found for biz_event_type:{}", bizEventParam.getBizEventType());
            throw new LogbookException(LogbookError.NOT_FOUND_PERMIT_RULE,
                    "not found biz_event_permit_config for biz_event_type:" + bizEventParam.getBizEventType());
        }

        // 3. 执行准入返回结果
        PermitMediator permitMediator = permitMediatorFactory.getPermitMediator(permitRuleConfig.getPermitType());
        if (null == permitMediator) {
            log.error("no permitMediator found for biz_event_type:{}", bizEventParam.getBizEventType());
            throw new LogbookException(LogbookError.NOT_FOUND_PERMIT_MEDIATOR,
                    "not found biz_event_permit_mediator for biz_event_type:" + bizEventParam.getBizEventType());
        }
        return permitMediator.permit(bizEventParam, permitRuleConfig);
    }


    /**
     * 业务事件 翻译
     *
     * @param callerInfo
     * @param bizEventParam
     * @return
     */
    @Override
    public TranslateVo translate(CallerInfo callerInfo, BizEventParam bizEventParam) {
        // 1. 参数检查
        translateParamCheck(callerInfo, bizEventParam);
        String globalBizEventId = String.format("%s.%s", callerInfo.getCaller(), bizEventParam.getBizEventId());

        // 2. 插入记录
        addBizEventProcessRecord(globalBizEventId, callerInfo, bizEventParam);

        // 3. 执行翻译
        List<TranslateAndSyncResult> translateAndSyncResults =
                executeTranslate(globalBizEventId, callerInfo, bizEventParam);

        // 4. 更新记录
        TranslateVo vo = updateBizEventProcessRecordAfterTrans(globalBizEventId, translateAndSyncResults);

        // 5. 返回结果
        return vo;
    }

    /**
     * 业务事件 上报
     *
     * @param callerInfo
     * @param eventParam
     * @return
     */
    @Override
    public ReportVo<Object> report(CallerInfo callerInfo, BizEventParam eventParam) {
        PermitVo<Object> permitVo = permit(callerInfo, eventParam);
        // permit 返回 null
        if (permitVo == null) {
            log.error("report do permit result is null");
            throw new LogbookException(LogbookError.UNEXPECTED_NULL_RESULT_PERMIT);
        }
        // permit 失败
        if (!permitVo.isSuccess()) {
            return ReportVo.builder().success(false).result(permitVo.getResult()).build();
        }

        TranslateVo translateVo = translate(callerInfo, eventParam);
        return buildReportResult(translateVo);
    }

    /**
     * 重试上报（核心是做翻译的重试）
     *
     * @param callerInfo
     * @param param
     * @return
     */
    @Override
    public ReportVo<Object> reportRetry(CallerInfo callerInfo, RetryReportParam param) {
        paramCheck(callerInfo, param);
        String globalBizEventId = param.getGlobalBizEventId();

        // 1. 查询记录
        BizEventProcessRecord recordInDb = getBizEventProcessRecordForReportRetry(globalBizEventId);

        // 2. 执行翻译
        BizEventParam bizEventParam = buildBizEventParam(recordInDb);
        List<TranslateAndSyncResult> translateAndSyncResults =
                executeTranslate(globalBizEventId, callerInfo, bizEventParam);

        // 3. 更新记录
        TranslateVo translateVo = updateBizEventProcessRecordAfterTrans(globalBizEventId, translateAndSyncResults);

        // 4. 返回结果
        return buildReportResult(translateVo);
    }


    // 执行翻译
    private List<TranslateAndSyncResult> executeTranslate(String globalBizEventId,
                                                          CallerInfo callerInfo, BizEventParam bizEventParam) {

        List<TranslateAndSyncResult> results = new ArrayList<>();

        // 1. 获取翻译列表
        List<TranslateRuleConfig> translateRuleConfigs = translateRuleConfigManager.
                getTranslateRuleConfig(bizEventParam.getBizEventType());
        if (null == translateRuleConfigs) {
            log.error("no translate config for biz_event_type:{}", bizEventParam.getBizEventType());
            throw new LogbookException(LogbookError.NOT_FOUND_TRANSLATE_RULE,
                    "no translate config for biz_event_type:" + bizEventParam.getBizEventType());
        }

        // 2. foreach【内部不抛异常，每一个异常分支都需要吞掉、记录下来在结果集中一起返回】
        translateRuleConfigs.forEach(translateRuleConfig -> {
            // 2.1 获取 translateMediator
            TranslateMediator translateMediator = getTranslateMediator(translateRuleConfig);
            if (null == translateMediator) {
                results.add(buildTranslateFail("no translateMediator for " +
                        "biz_event_type:" + bizEventParam.getBizEventType()));
                return;
            }

            // 2.2 执行 translate
            TranslateInput translateInput = buildTranslateInput(globalBizEventId, callerInfo, bizEventParam);
            SingleTranslateVo<Object> singleTranslateVo =
                    executeTranslate(translateRuleConfig, translateInput, translateMediator);
            if (null == singleTranslateVo || !singleTranslateVo.isSuccess()) {
                results.add(buildTranslateFail(null == singleTranslateVo
                        ? "translate result is null" : singleTranslateVo.getResult().toString()));
                return;
            }

            // 2.3 反序列化
            EntityEventDto entityEventDto =
                    convertToEntityEventDto(translateInput, translateRuleConfig, singleTranslateVo.getResult());
            if (null == entityEventDto) {
                results.add(buildTranslateFail("translate result can not convert to  entityEventDto"));
                return;
            }

            // 2.4 获取 mappingEntityProduce
            MappingEntityProducer mappingEntityProduce = getProducerConfig(entityEventDto);
            if (null == mappingEntityProduce) {
                results.add(buildProduceFail(entityEventDto,
                        "no producer config for entity_event_type:" + entityEventDto.getEntityType()));
                return;
            }

            // 2.5 执行 produce
            ProduceVo<Object> produceVo = doProduce(entityEventDto, mappingEntityProduce);
            if (null == produceVo || !produceVo.isSuccess()) {
                results.add(buildProduceFail(entityEventDto, produceVo == null
                        ? "produce result is null or fail" : produceVo.getResult().toString()));
                return;
            }

            // 2.6 全部成功 返回结果
            results.add(buildSuccess(produceVo, entityEventDto));
        });

        return results;
    }


    // --------------------------------------------------------
    // exception catcher method
    // --------------------------------------------------------

    // 执行 produce + 异常处理
    private ProduceVo<Object> doProduce(EntityEventDto entityEventDto, MappingEntityProducer mappingEntityProduceSo) {
        try {
            return producerConfigManager
                    .produce(mappingEntityProduceSo.getTopicName(), buildMessageBody(entityEventDto));
        } catch (Exception e) {
            log.error("doProduce failed,entityEventDto={},mappingEntityTypeProducerSo={}," +
                    "e=", entityEventDto, mappingEntityProduceSo, e);
            return ProduceVo.builder().success(false).result("doProduce caught exception:" + e.getMessage()).build();
        }
    }

    // 获取 ProducerConfig + 异常处理
    private MappingEntityProducer getProducerConfig(EntityEventDto entityEventDto) {
        try {
            return mappingEntityProducerConfigManager.getProducerConfig(entityEventDto.getEntityType());
        } catch (Exception e) {
            log.error("getProducerConfig failed,entityEventDto={},e=", entityEventDto, e);
            return null;
        }
    }

    // 执行 translate + 异常处理
    private SingleTranslateVo<Object> executeTranslate(TranslateRuleConfig translateRuleConfig,
                                                       TranslateInput translateInput,
                                                       TranslateMediator translateMediator) {
        try {
            return translateMediator.translate(translateRuleConfig, translateInput);
        } catch (Exception e) {
            log.error("doTranslate failed,translateRuleConfig={},translateInput={}," +
                    "e=", translateRuleConfig, translateInput, e);
            return SingleTranslateVo.builder().success(false)
                    .result("doTranslate caught exception：" + e.getMessage()).build();
        }
    }

    // 获取 TranslateMediator + 异常处理
    private TranslateMediator getTranslateMediator(TranslateRuleConfig translateRuleConfig) {
        try {
            return translateMediatorFactory.getTranslateMediator(translateRuleConfig.getTranslateType());
        } catch (Exception e) {
            log.error("getTranslateMediator failed,translateRuleConfig={},e=", translateRuleConfig, e);
            return null;
        }
    }


    // ----------------------
    // serialize and deserialize
    // ----------------------

    // EntityEventDto 序列化 String
    private String buildMessageBody(EntityEventDto entityEventDto) {
        try {
            return objectMapper.writeValueAsString(entityEventDto);
        } catch (JsonProcessingException e) {
            log.error("buildMessageBody failed,e=", e);
            throw new LogbookException(LogbookError.FAILED_SERIALIZE_TO_STRING);
        }
    }

    // Object 反序列化 EntityEventDto
    private EntityEventDto convertToEntityEventDto(TranslateInput translateInput,
                                                   TranslateRuleConfig translateRuleConfig, Object result) {
        try {
            TranslateOutput output =
                    objectMapper.readValue(objectMapper.writeValueAsString(result), TranslateOutput.class);
            return buildEntityEventDto(translateInput, translateRuleConfig, output);
        } catch (Exception e) {
            log.error("convertToEntityEventDto caught exception,result={},e=", result, e);
            return null;
        }
    }

    // BizEventProcessRecord.bizEventBody 反序列化为 BizEventParam
    private BizEventParam buildBizEventParam(BizEventProcessRecord recordInDb) {
        try {
            return objectMapper.readValue(recordInDb.getBizEventBody(), BizEventParam.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LogbookException(LogbookError.FAILED_DESERIALIZE_FROM_STRING);
        }
    }


    // --------------------------------------------------------
    // add or update database
    // --------------------------------------------------------

    // 插入 biz_event_process 记录
    private void addBizEventProcessRecord(String globalBizEventId, CallerInfo callerInfo, BizEventParam bizEventParam) {

        BizEventProcessRecord bizEventProcessRecord =
                buildUnProcessBizEventRecord(globalBizEventId, callerInfo, bizEventParam);
        int effect = bizEventRecordStorage.insert(bizEventProcessRecord);

        // 插入成功，继续后续流程
        if (effect != 0) {
            return;
        }

        BizEventProcessRecord recordInDb = bizEventRecordStorage.queryByBizEventId(globalBizEventId);
        // 插入失败、查询失败，返回 数据库异常
        if (recordInDb == null) {
            log.warn("addBizEventProcessRecord conflict,query fail");
            throw new LogbookException(LogbookError.INSERT_AND_QUERY_FAILED);
        }

        // 插入失败，查询已成功，返回 已成功
        if (LogbookConstants.BIZ_EVENT_PROCESS_COMPLETED == recordInDb.getStatus()) {
            log.warn("addBizEventProcessRecord conflict,already success");
            throw new LogbookException(LogbookError.SUCCEED);
        }

        // 插入失败，查询待处理，返回 流程中
        log.warn("addBizEventProcessRecord conflict,already in processing");
        throw new LogbookException(LogbookError.IN_PROCESSING_TRANSLATE_WAIT_RETRY);
    }


    // 更新 biz_event_process 记录
    private TranslateVo updateBizEventProcessRecordAfterTrans(String globalBizEventId,
                                                              List<TranslateAndSyncResult> translateAndSyncResults) {
        boolean allTranslateSuccess = true;
        for (TranslateAndSyncResult translateAndSyncResult : translateAndSyncResults) {
            if (!translateAndSyncResult.isFinalSuccess()) {
                allTranslateSuccess = false;
                break;
            }
        }

        // 如果都翻译成功，更新数据库记录的状态为"已完成"
        // 如果不是都翻译成功，不抛异常，状态不改，仅记录 error 日志
        if (allTranslateSuccess) {
            BizEventProcessRecord bizEventProcessRecord = new BizEventProcessRecord();
            bizEventProcessRecord.setGlobalBizEventId(globalBizEventId);
            bizEventProcessRecord.setStatus(LogbookConstants.BIZ_EVENT_PROCESS_COMPLETED);
            bizEventProcessRecord.setSuccessTime(new Date());
            bizEventRecordStorage.update(bizEventProcessRecord, LogbookConstants.BIZ_EVENT_PROCESS_WAIT_START);
        } else {
            log.error("updateBizEventProcessRecordAfterTrans not all success," +
                    "translateAndSyncResults={}", translateAndSyncResults);
        }

        return TranslateVo.builder().allSuccess(allTranslateSuccess)
                .translateAndSyncResults(translateAndSyncResults).build();
    }

    // 重试时，从数据库查询记录
    private BizEventProcessRecord getBizEventProcessRecordForReportRetry(String globalBizEventId) {
        BizEventProcessRecord recordInDb = bizEventRecordStorage.queryByBizEventId(globalBizEventId);

        if (null == recordInDb) {
            log.error("getBizEventProcessRecordForReportRetry failed,no record");
            throw new LogbookException(LogbookError.NOT_FOUND_REPORT_RECORD);
        }
        if (recordInDb.getStatus() == LogbookConstants.BIZ_EVENT_PROCESS_COMPLETED) {
            log.warn("getBizEventProcessRecordForReportRetry success,but report already completed");
            throw new LogbookException(LogbookError.SUCCEED);
        }
        if (recordInDb.getStatus() == LogbookConstants.BIZ_EVENT_PROCESS_ABANDONED) {
            log.warn("getBizEventProcessRecordForReportRetry success,but report already abandoned");
            throw new LogbookException(LogbookError.ALREADY_REPORT_ABANDON);
        }
        return recordInDb;
    }

    // ----------------------
    // param check
    // ----------------------

    private void permitParamCheck(CallerInfo callerInfo, BizEventParam bizEventParam) {
        if (null == callerInfo || null == bizEventParam) {
            log.warn("permitParamCheck failed,callerInfo={},bizEventParam={}", callerInfo, bizEventParam);
            throw new LogbookException(LogbookError.PARAM_INVALID);
        }
    }

    private void translateParamCheck(CallerInfo callerInfo, BizEventParam bizEventParam) {
        if (null == callerInfo || null == bizEventParam) {
            log.warn("translateParamCheck failed,callerInfo={},bizEventParam={}", callerInfo, bizEventParam);
            throw new LogbookException(LogbookError.PARAM_INVALID);
        }
    }


    private void paramCheck(CallerInfo callerInfo, RetryReportParam param) {
        if (null == callerInfo || null == param || StringUtils.isEmpty(param.getGlobalBizEventId())) {
            log.warn("paramCheck failed,callerInfo={},param={}", callerInfo, param);
            throw new LogbookException(LogbookError.PARAM_INVALID);
        }
    }


    // --------------------------------------------------------
    // input or output builder
    // --------------------------------------------------------

    // todo 有效性校验
    private EntityEventDto buildEntityEventDto(TranslateInput input,
                                               TranslateRuleConfig translateRuleConfig, TranslateOutput output) {
        EntityEventDto dto = new EntityEventDto();
        dto.setEventType(output.getEventType());
        dto.setEventTime(output.getEventTime() <= 0 ? System.currentTimeMillis() : output.getEventTime());
        dto.setEventInfo(output.getEventInfo());
        dto.setEntityId(output.getEntityId());
        dto.setEntityType(getEntityTypeId(output));
        dto.setEntityInfoBeforeEvent(output.getEntityInfoBeforeEvent());
        dto.setEntityInfoAfterEvent(output.getEntityInfoAfterEvent());
        String globalEventId = String.format("%s.%s", input.getGlobalBizEventId(), translateRuleConfig.getId());
        dto.setEventId(globalEventId);
        dto.setTraceId(TraceUtil.getTraceEntry().getTraceId());
        dto.setSpanId(TraceUtil.getTraceEntry().getSpanId());
        dto.setEventReportSystem(input.getCallerName());
        return dto;
    }

    private Long getEntityTypeId(TranslateOutput output) {
        if (entityEventTypeManager.isExist(output.getEventType())) {
            return entityEventTypeManager.getEntityTypeId(output.getEventType());
        } else {
            throw new IllegalArgumentException("invalid entity_type:" + output.getEventType());
        }
    }

    // 构造 TranslateInput
    private TranslateInput buildTranslateInput(String globalBizEventId,
                                               CallerInfo callerInfo, BizEventParam bizEventParam) {
        TranslateInput input = new TranslateInput();
        input.setBizEventType(bizEventParam.getBizEventType());
        input.setBizEventId(bizEventParam.getBizEventId());
        input.setBizEventInfo(bizEventParam.getBizEventInfo());
        input.setBizEntityInfo(bizEventParam.getBizEntityInfo());
        input.setBizEntityInfoBefore(bizEventParam.getBizEntityInfoBefore());
        input.setGlobalBizEventId(globalBizEventId);
        input.setCallerName(callerInfo.getCaller());
        input.setTraceId(TraceUtil.getTraceEntry().getTraceId());
        input.setSpanId(TraceUtil.getTraceEntry().getSpanId());
        return input;
    }


    // 构造 biz_event_record 记录
    private BizEventProcessRecord buildUnProcessBizEventRecord(String globalBizEventId,
                                                               CallerInfo callerInfo,
                                                               BizEventParam bizEventParam) {
        BizEventProcessRecord bizEventProcessRecord = new BizEventProcessRecord();
        bizEventProcessRecord.setGlobalBizEventId(globalBizEventId);
        bizEventProcessRecord.setCallerName(StringUtils.defaultString(callerInfo.getCaller()));
        bizEventProcessRecord.setBizEventBody(StringUtils.defaultString(getString(bizEventParam)));
        bizEventProcessRecord.setTraceId(StringUtils.defaultString(TraceUtil.getTraceEntry().getTraceId()));
        bizEventProcessRecord.setSpanId(StringUtils.defaultString(TraceUtil.getTraceEntry().getSpanId()));
        bizEventProcessRecord.setStatus(LogbookConstants.BIZ_EVENT_PROCESS_WAIT_START);
        bizEventProcessRecord.setCreateTime(new Date());

        return bizEventProcessRecord;
    }

    // BizEventParam 转 String
    private String getString(BizEventParam bizEventParam) {
        try {
            return objectMapper.writeValueAsString(bizEventParam);
        } catch (JsonProcessingException e) {
            log.error("getString caught exception,e=", e);
            throw new LogbookException(LogbookError.FAILED_SERIALIZE_TO_STRING);
        }
    }

    // 翻译失败 => 最终失败
    private TranslateAndSyncResult buildTranslateFail(String failReason) {
        TranslateAndSyncResult result = new TranslateAndSyncResult();
        result.setTranslateSuccess(false);
        result.setTranslateFailReason(failReason);
        result.setFinalSuccess(false);// 最终失败
        return result;
    }


    // 翻译成功，produce 失败 => 最终失败
    private TranslateAndSyncResult buildProduceFail(EntityEventDto entityEventDto, String failReason) {
        TranslateAndSyncResult result = new TranslateAndSyncResult();
        result.setTranslateSuccess(true);
        result.setTranslateSuccessResult(entityEventDto);
        result.setProduceSuccess(false);
        result.setProduceFailReason(failReason);
        result.setFinalSuccess(false);// 最终失败
        return result;
    }


    // 翻译成功，produce 成功 => 最终成功
    private TranslateAndSyncResult buildSuccess(ProduceVo<Object> produceVo, EntityEventDto entityEventDto) {
        TranslateAndSyncResult result = new TranslateAndSyncResult();
        result.setTranslateSuccess(true);
        result.setTranslateSuccessResult(entityEventDto);
        result.setProduceSuccess(true);
        result.setProduceSuccessResult(produceVo);
        result.setFinalSuccess(true);// 最终成功
        return result;
    }


    // 构造 ReportVo
    private ReportVo<Object> buildReportResult(TranslateVo translateVo) {
        // translate 返回 null
        if (translateVo == null) {
            log.error("report do translate result is null");
            throw new LogbookException(LogbookError.UNEXPECTED_NULL_RESULT_TRANSLATE);
        }
        // translate 失败
        if (!translateVo.isAllSuccess()) {
            return ReportVo.builder().success(false)
                    .translateAndSyncResults(translateVo.getTranslateAndSyncResults()).build();
        }
        // translate 成功
        return ReportVo.builder().success(true)
                .translateAndSyncResults(translateVo.getTranslateAndSyncResults()).build();
    }


}

