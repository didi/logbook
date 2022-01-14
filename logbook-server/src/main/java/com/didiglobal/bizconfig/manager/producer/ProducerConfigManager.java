package com.didiglobal.bizconfig.manager.producer;

import com.didiglobal.common.pojo.vo.ProduceVo;
import com.didiglobal.mybatis.entity.MetaProducer;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/15 8:17 下午
 */
public interface ProducerConfigManager {

    void refreshProducerConfigMap(Map<String, MetaProducer> map);

    ProduceVo<Object> produce(String topicName, String messageBody);

}
