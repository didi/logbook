package com.didiglobal.mq.consumer;

import com.didiglobal.api.biz.service.ConsumerProcessor;
import com.didiglobal.mybatis.entity.MetaConsumer;

/**
 * @author liyanling
 * @date 2021/11/18 4:36 下午
 */
public interface ConsumeGenerator<T> {

    // 生成 IConsumer 实例
    IConsumer generate(MetaConsumer consumer, ConsumerProcessor<T> processor, Object... params);

}
