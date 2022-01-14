package com.didiglobal.bizconfig.manager.entitytype;

import com.didiglobal.mybatis.entity.MappingEntityProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/15 8:19 下午
 */
@Component
@Slf4j
public class MappingEntityProducerConfigManagerImpl implements MappingEntityProducerConfigManager {
    private Map<Long, MappingEntityProducer> entityEventTypeProducerConfigMap;

    public MappingEntityProducerConfigManagerImpl() {
        this.entityEventTypeProducerConfigMap = new ConcurrentHashMap<>();
    }

    @Override
    public MappingEntityProducer getProducerConfig(Long entityTypeId) {
        return entityEventTypeProducerConfigMap.get(entityTypeId);
    }

    @Override
    public void refreshEntityEventTypeProducerConfigMap(Map<Long, MappingEntityProducer> map) {
        this.entityEventTypeProducerConfigMap = map;
        log.info("MappingEntityProducerConfigManagerImpl refresh success,size={}",
                this.entityEventTypeProducerConfigMap.size());
    }
}
