package com.didiglobal.mq.producer.cust;

import com.didiglobal.mq.MQCustomizedUtil;
import com.didiglobal.mybatis.entity.MetaProducer;
import com.didiglobal.mq.producer.IProducer;
import com.didiglobal.mq.producer.ProducerGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * @author liyanling
 * @date 2021/11/17 8:18 下午
 */
@Slf4j
@Component
public class LogbookProducerGenerator implements ProducerGenerator<Object> {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public IProducer<Object> generateProducer(MetaProducer metaProducer) {
        Properties properties = buildProperties(metaProducer);
        if (properties == null) {
            log.error("generateProducer fail, properties is null");
            throw new IllegalArgumentException("producerConfig invalid,can not convert to properties");
        }
        Object realityProducer = MQCustomizedUtil.doCreateProducer(properties);
        return new LogbookProducer(realityProducer);
    }

    private Properties buildProperties(MetaProducer metaProducer) {
        try {
            return objectMapper.readValue(metaProducer.getTopicContext(), Properties.class);
        } catch (IOException e) {
            log.error("buildProperties caught exception,e=", e);
            throw new IllegalArgumentException("buildProperties caught exception:" + e.getMessage());
        }
    }

}
