package com.didiglobal.mq.consumer.cust;

import com.didiglobal.api.biz.service.ConsumerProcessor;
import com.didiglobal.common.pojo.CommonConsumerRecord;
import com.didiglobal.common.pojo.ConsumeMessageContext;
import com.didiglobal.common.pojo.dto.EntityEventDto;
import com.didiglobal.common.pojo.vo.ConsumeVo;
import com.didiglobal.config.mqplugins.MessageQueueTraceContext;
import com.didiglobal.config.mqplugins.MessageQueueTraceProcessor;
import com.didiglobal.mq.MQCustomizedUtil;
import com.didiglobal.mybatis.entity.MetaConsumer;
import com.didiglobal.mq.consumer.IConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.*;

/**
 * @author liyanling
 * @date 2021/11/18 4:39 下午
 * <p>
 * 不需要 spring 托管，由 generator 管理
 */
@Slf4j
public class LogbookConsumer implements IConsumer {
    private volatile boolean isRunnable = false;// 任务运行状态标识
    private final Object realityConsumer;
    private final MetaConsumer metaConsumer;
    private final Properties properties;
    private final ApplicationContext applicationContext;


    public LogbookConsumer(ApplicationContext applicationContext, MetaConsumer metaConsumer,
                           Object consumer, Properties properties) {
        this.applicationContext = applicationContext;
        this.metaConsumer = metaConsumer;
        this.realityConsumer = consumer;
        this.properties = properties;
    }

    /**
     * 开始消费消息
     *
     * @param params
     */
    @Override
    public void startConsume(Object... params) {
        doSubscribe();
        if (!isRunnable) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    log.info("consumer start:{}", metaConsumer);
                    isRunnable = true;
                    while (isRunnable) {
                        // 消费一组数据
                        Map<String, CommonConsumerRecord> recordMap = consumeOnce();
                        if (null == recordMap) {
                            continue;
                        }
                        // 逐个处理
                        for (CommonConsumerRecord record : recordMap.values()) {
                            dealConsumerRecords(record);
                        }
                    }
                    doClose();
                    log.info("consumer close:{}", metaConsumer);
                }
            };
            applicationContext.getBean(ThreadPoolTaskExecutor.class).submit(runnable);
        }
    }


    @Override
    public synchronized void close() {
        this.isRunnable = false;
    }


    /**
     * 处理消费到的消息
     *
     * @param record
     */
    private void dealConsumerRecords(CommonConsumerRecord record) {

        // 1. 获取 processor 实例
        ConsumerProcessor<Object> consumerProcessor = applicationContext.getBean(ConsumerProcessor.class);
        MessageQueueTraceProcessor traceProcessor = applicationContext.getBean(MessageQueueTraceProcessor.class);

        // 2. 反序列化，构建上下文信息
        // TODO: 如果反序列话失败（比如上游传个null），当前表现是卡在这里了
        ConsumeMessageContext consumeMessageContext = buildConsumeContext(record);
        if (consumeMessageContext == null) {
            log.error(Thread.currentThread() + " dealConsumerRecords deserialize failed,record={}", record);
            return;
        }

        // 3. 执行业务逻辑
        MessageQueueTraceContext traceContext = initTraceContext(consumeMessageContext, this.metaConsumer);
        traceProcessor.beforeConsumeProcess(traceContext);
        try {
            ConsumeVo<Object> consumeVo = consumerProcessor.consume(consumeMessageContext);
            traceContext.setConsumeVo(consumeVo);
            // 明确给结果、需要重试时，重试
            if (null != consumeVo && consumeVo.isNeedRetry()) {
                consumerProcessor.retryConsume(consumeMessageContext);
            }
        } catch (Exception e) {
            // Exception 异常，重试
            // consume() 和 retryConsume() 都做了 catch Exception，理论上这里不会再有异常，纯兜底
            traceContext.setFailureException(e);
            consumerProcessor.retryConsume(consumeMessageContext);
        } finally {
            traceProcessor.afterConsumeProcess(traceContext);
        }
    }


    // 初始化 trace 信息
    private MessageQueueTraceContext initTraceContext(ConsumeMessageContext messageContext, MetaConsumer metaConsumer) {
        MessageQueueTraceContext traceContext = new MessageQueueTraceContext();
        traceContext.setTraceId(messageContext.getConsumeTraceId());
        traceContext.setSpanId(messageContext.getConsumeSpanId());
        traceContext.setConsumerGroupName(metaConsumer.getConsumerGroupName());
        traceContext.setConsumerTopicName(messageContext.getTopicName());
        traceContext.setThreadName(Thread.currentThread().getName());
        traceContext.setConsumeMessage(messageContext.getEntityEventDto().toString());
        traceContext.setTimestamp(System.currentTimeMillis());
        traceContext.setSuccess(true);
        return traceContext;
    }


    // 构建 ConsumeContext
    private ConsumeMessageContext buildConsumeContext(CommonConsumerRecord record) {
        EntityEventDto entityEventDto = null;
        try {
            entityEventDto = deserialize(record.getValue());
        } catch (IOException e) {
            log.error("buildConsumeContext caught exception,record={},e=", record, e);
            return null;
        }

        // traceid 使用原来的 spanid
        String consumeTraceId = entityEventDto.getSpanId();
        // spanid 使用 当前线程名.原来的 spanid
        String consumeSpanId = String.format("%s.%s", Thread.currentThread().getName(), entityEventDto.getSpanId());
        // 消费事件id 使用 消费组名.实体事件id
        String consumeGlobalEventId = String.format("%s.%s",
                this.metaConsumer.getConsumerGroupName(), entityEventDto.getEventId());

        return ConsumeMessageContext.builder()
                .topicName(record.getTopicName())
                .threadName(Thread.currentThread().getName())
                .consumeTraceId(consumeTraceId)
                .consumeSpanId(consumeSpanId)
                .entityEventDto(entityEventDto)
                .consumeGroupName(this.metaConsumer.getConsumerGroupName())
                .consumeGlobalEventId(consumeGlobalEventId)
                .consumeMessageBody(record.getValue())
                .build();
    }


    // 反序列化
    private EntityEventDto deserialize(String value) throws IOException {
        return applicationContext.getBean(ObjectMapper.class).readValue(value, EntityEventDto.class);
    }


    private void doSubscribe() {
        // 订阅的 topic 是在 properties 里用 CUSTOMIZE_CONSUMER_TOPICS 配置的
        MQCustomizedUtil.doConsumerSubscribe(properties);
    }

    private Map<String, CommonConsumerRecord> consumeOnce() {
        return MQCustomizedUtil.doConsumeOnce(realityConsumer);
    }

    private void doClose() {
        MQCustomizedUtil.doCloseConsumer(realityConsumer);
    }

}
