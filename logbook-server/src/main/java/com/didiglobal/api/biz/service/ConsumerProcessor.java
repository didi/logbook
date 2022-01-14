package com.didiglobal.api.biz.service;

import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.param.RetryConsumeParam;
import com.didiglobal.common.pojo.ConsumeMessageContext;
import com.didiglobal.common.pojo.vo.ConsumeVo;

/**
 * @author liyanling
 * @date 2021/11/18 4:09 下午
 */
public interface ConsumerProcessor<T> {

    // 给消息，自己处理
    ConsumeVo<T> consume(ConsumeMessageContext consumeMessageContext, Object... params);

    // consume 消费失败，重试
    // 目前的方案是重新推到 kafka 的 topic 里
    void retryConsume(ConsumeMessageContext consumeMessageContext, Object... params);

    // 重试 分发
    ConsumeVo<Object> consumeRetry(CallerInfo callerInfo, RetryConsumeParam param);
}
