package com.didiglobal.mq.producer.cust;

import com.didiglobal.common.pojo.vo.ProduceVo;
import com.didiglobal.mq.MQCustomizedUtil;
import com.didiglobal.mq.producer.IProducer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liyanling
 * @date 2021/11/17 5:58 下午
 * <p>
 * 不需要 spring 托管，由 generator 管理
 */
@Slf4j
public class LogbookProducer implements IProducer<Object> {

    private final Object realityProducer;

    public LogbookProducer(Object realityProducer) {
        this.realityProducer = realityProducer;
    }

    @Override
    public ProduceVo<Object> produce(String topicName, String messageBody, Object... params) {
        if (null == realityProducer) {
            return ProduceVo.builder().success(false).result("producer is null").build();
        }
        try {
            ProduceVo<Object> result = produceResult(topicName, messageBody);
            return ProduceVo.builder().success(true).result(result).build();
        } catch (Exception e) {
            log.error("produce caught exception,e=", e);
            return ProduceVo.builder().success(false).result("produce caught exception:" + e.getMessage()).build();
        }
    }


    private ProduceVo<Object> produceResult(String topicName, String messageBody) {
        MQCustomizedUtil.doProduce(realityProducer, topicName, messageBody);
        return null;
    }

    @Override
    public void close() {
        MQCustomizedUtil.doCloseProducer(realityProducer);
    }

}
