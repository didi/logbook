package com.didiglobal.config.mqplugins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liyanling
 * @date 2021/11/23 6:24 下午
 */
@Component
@Slf4j
public class MessageQueueTraceProcessorImpl implements MessageQueueTraceProcessor {
    public static final String MQ_CONSUME_START_TAG = "_com_mq_consume_start";
    public static final String MQ_CONSUME_END_TAG = "_com_mq_consume_end";


    // 消费业务逻辑执行前
    @Override
    public void beforeConsumeProcess(MessageQueueTraceContext traceContext) {
        traceContext.setTag(MQ_CONSUME_START_TAG);
        traceContext.setTimestamp(System.currentTimeMillis());
        log.info(genString(traceContext));
    }


    // 消费业务逻辑执行后
    @Override
    public void afterConsumeProcess(MessageQueueTraceContext traceContext) {
        traceContext.setTag(MQ_CONSUME_END_TAG);
        traceContext.setCost((int) (System.currentTimeMillis() - traceContext.getTimestamp()));
        traceContext.setTimestamp(System.currentTimeMillis());
        log.info(genString(traceContext));
    }


    private String genString(MessageQueueTraceContext traceContext) {
        if(traceContext.getFailureException()!=null){
            traceContext.setSuccess(false);
        }
        if(traceContext.getConsumeVo()==null || !traceContext.getConsumeVo().isSuccess()){
            traceContext.setSuccess(false);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(traceContext.getTag())
                .append(", traceId=").append(traceContext.getTraceId())
                .append(", spanId=").append(traceContext.getSpanId())
                .append(", threadName=").append(traceContext.getThreadName())
                .append(", consumerGroupName=").append(traceContext.getConsumerGroupName())
                .append(", consumerTopicName=").append(traceContext.getConsumerTopicName())
                .append(", timestamp=").append(traceContext.getTimestamp())
                .append(", costTime=").append(traceContext.getCost())
                .append(", consumeMessage=").append(traceContext.getConsumeMessage())
                .append(", success=").append(traceContext.isSuccess());

        if (traceContext.getFailureException() != null) {
            sb.append(", exception=").append(traceContext.getFailureException().toString());
        }
        if (traceContext.getConsumeVo() != null) {
            sb.append(", consume_vo=").append(traceContext.getConsumeVo());
        }
        return sb.toString();
    }

}
