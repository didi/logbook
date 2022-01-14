package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.entitytype.MappingEntityProducerConfigManager;
import com.didiglobal.mybatis.entity.MappingEntityProducer;
import com.didiglobal.mybatis.mapper.MappingEntityProduceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/17 4:08 下午
 */
@Slf4j
@Component
@EnableScheduling
public class MappingEntityProducerLoader {

    @Resource
    MappingEntityProduceMapper mappingEntityProduceMapper;

    @Resource
    MappingEntityProducerConfigManager mappingEntityProducerConfigManager;

    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<MappingEntityProducer> configs = mappingEntityProduceMapper.queryAllMappingItem();
        mappingEntityProducerConfigManager.refreshEntityEventTypeProducerConfigMap(getConfigMap(configs));
        log.info("MappingEntityTypeProducerLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${mapping_entity_producer_load_cron}")
    public synchronized void loadOnTime() {
        log.info("MappingEntityTypeProducerLoader loadOnTime start!");
        List<MappingEntityProducer> configs = mappingEntityProduceMapper.queryAllMappingItem();
        if(configs == null || configs.isEmpty()) {
            log.info("MappingEntityTypeProducerLoader loadOnTime break,configs.size=0");
            return;
        }
        mappingEntityProducerConfigManager.refreshEntityEventTypeProducerConfigMap(getConfigMap(configs));
        log.info("MappingEntityTypeProducerLoader loadOnTime success,configs.size={}", configs.size());
    }

    private Map<Long, MappingEntityProducer> getConfigMap(List<MappingEntityProducer> configs) {
        Map<Long, MappingEntityProducer> map = new ConcurrentHashMap<>();
        configs.forEach(p->{
            map.put(p.getEntityTypeId(), p);
        });
        return map;
    }
}
