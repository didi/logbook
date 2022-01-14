package com.didiglobal.mq.consumer.cust;

import com.didiglobal.api.biz.service.ConsumerProcessor;
import com.didiglobal.mq.MQCustomizedUtil;
import com.didiglobal.mybatis.entity.MetaConsumer;
import com.didiglobal.mq.consumer.ConsumeGenerator;
import com.didiglobal.mq.consumer.IConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * @author liyanling
 * @date 2021/11/18 4:59 下午
 */
@Slf4j
@Component
public class LogbookConsumeGenerator implements ConsumeGenerator<Object> {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public IConsumer generate(MetaConsumer metaConsumer, ConsumerProcessor<Object> processor, Object... params) {
        Properties properties = buildProperties(metaConsumer);
        return new LogbookConsumer(applicationContext, metaConsumer, MQCustomizedUtil.doCreateConsumer(properties), properties);
    }

    private Properties buildProperties(MetaConsumer metaConsumer) {
        try {
            return objectMapper.readValue(metaConsumer.getConsumerContext(), Properties.class);
        } catch (IOException e) {
            log.error("buildProperties caught exception,e=", e);
            throw new IllegalArgumentException("buildProperties caught exception:" + e.getMessage());
        }
    }
}
