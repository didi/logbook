package com.didiglobal.bizconfig.manager.entitytype;

import com.didiglobal.mybatis.entity.MappingEntityProducer;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/15 8:17 下午
 */
public interface MappingEntityProducerConfigManager {

    /**
     * 根据 实体事件类型 获得 producerConfig
     *
     * @param entityTypeId
     * @return
     */
    MappingEntityProducer getProducerConfig(Long entityTypeId);

    void refreshEntityEventTypeProducerConfigMap(Map<Long, MappingEntityProducer> map);
}
