package com.didiglobal.config.mqplugins;

/**
 * @author liyanling
 * @date 2021/11/23 5:22 下午
 */
public interface MessageQueueTraceProcessor {

    // 消费业务逻辑执行前
    void beforeConsumeProcess(MessageQueueTraceContext traceContext);

    // 消费业务逻辑执行后
    void afterConsumeProcess(MessageQueueTraceContext traceContext);
}
