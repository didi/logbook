package com.didiglobal.mq.producer;

import com.didiglobal.mybatis.entity.MetaProducer;

/**
 * @author liyanling
 * @date 2021/11/17 7:52 下午
 *
 * IProducer 生成器
 */
public interface ProducerGenerator<T> {

    // 生成 IProducer 实例
    IProducer<T> generateProducer(MetaProducer metaProducer);
}
