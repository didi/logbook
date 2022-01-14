package com.didiglobal.mq.consumer;

/**
 * @author liyanling
 * @date 2021/11/18 4:35 下午
 */
public interface IConsumer {

    // 启动消费
    void startConsume(Object... params);

    // 关闭消费
    void close();
}
