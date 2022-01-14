package com.didiglobal.mq.producer;

import com.didiglobal.common.pojo.vo.ProduceVo;

/**
 * @author liyanling
 * @date 2021/11/17 5:41 下午
 */
public interface IProducer<T> {

    // 生产消息
    ProduceVo<T> produce(String topicName,String messageBody,Object... params);

    // 关闭生产者
    void close();
}
