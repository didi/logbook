package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.producer.ProducerConfigManager;
import com.didiglobal.mybatis.entity.MetaProducer;
import com.didiglobal.mybatis.mapper.MetaProducerMapper;
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
public class MetaProducerConfigLoader {

    @Resource
    MetaProducerMapper metaProducerMapper;

    @Resource
    ProducerConfigManager producerConfigManager;

    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<MetaProducer> configs = metaProducerMapper.queryAllValidProducerConfig();
        producerConfigManager.refreshProducerConfigMap(getConfigMap(configs));
        log.info("ProducerConfigLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${meta_producer_load_cron}")
    public synchronized void loadOnTime() {
        log.info("ProducerConfigLoader loadOnTime start!");
        List<MetaProducer> configs = metaProducerMapper.queryAllValidProducerConfig();
        if(configs == null || configs.isEmpty()) {
            log.info("ProducerConfigLoader loadOnTime break,configs.size=0");
            return;
        }
        producerConfigManager.refreshProducerConfigMap(getConfigMap(configs));
        log.info("ProducerConfigLoader loadOnTime success,configs.size={}", configs.size());
    }

    private Map<String, MetaProducer> getConfigMap(List<MetaProducer> configs) {
        Map<String, MetaProducer> map = new ConcurrentHashMap<>();
        configs.forEach(p->{
            map.put(p.getTopicName(), p);
        });
        return map;
    }
}
