package com.didiglobal.bizconfig.manager.producer;

import com.didiglobal.common.pojo.vo.ProduceVo;
import com.didiglobal.mybatis.entity.MetaProducer;
import com.didiglobal.mq.producer.IProducer;
import com.didiglobal.mq.producer.ProducerGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/15 8:19 下午
 */
@Component
@Slf4j
public class ProducerConfigManagerImpl implements ProducerConfigManager {
    private Map<String, MetaProducer> producerConfigMap;

    @Autowired
    ProducerGenerator<Object> producerGenerator;

    public ProducerConfigManagerImpl() {
        this.producerConfigMap = new ConcurrentHashMap<>();
    }

    @Override
    public void refreshProducerConfigMap(Map<String, MetaProducer> map) {
        this.producerConfigMap = map;
        log.info("ProducerConfigManagerImpl refresh success,size={}", this.producerConfigMap.size());
    }

    // generate -> produce -> close
    @Override
    public ProduceVo<Object> produce(String topicName, String messageBody) {
        //todo 如果topicName没有创建的话，会有问题，抛空指针
        IProducer<Object> producer = producerGenerator.generateProducer(producerConfigMap.get(topicName));
        if (producer == null) {
            log.error("no producer instance for topicName:{}", topicName);
            return ProduceVo.builder().success(false).build();
        }
        ProduceVo<Object> produceVo = producer.produce(topicName, messageBody);
        producer.close();
        return produceVo;
    }
}
