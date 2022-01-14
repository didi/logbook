package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.consumer.MetaConsumerConfigManager;
import com.didiglobal.mybatis.entity.MetaConsumer;
import com.didiglobal.mybatis.mapper.MetaConsumerMapper;
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
 * @date 2021/11/16 3:51 下午
 */
@Component
@EnableScheduling
@Slf4j
public class MetaConsumerLoader {

    @Resource
    MetaConsumerMapper metaConsumerMapper;

    @Resource
    MetaConsumerConfigManager metaConsumerConfigManager;


    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<MetaConsumer> configs = metaConsumerMapper.queryValidMetaConsumer();
        metaConsumerConfigManager.refreshConsumerConfig(true,getCallerConfigMap(configs));
        log.info("MappingConsumeRuleLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${meta_consumer_load_cron}")
    public synchronized void loadOnTime() {
        log.info("MappingConsumeRuleLoader loadOnTime start");
        List<MetaConsumer> configs = metaConsumerMapper.queryValidMetaConsumer();
        if(configs == null || configs.isEmpty()) {
            log.info("MappingConsumeRuleLoader loadOnTime break,permitConfigs.size=0");
            return;
        }
        metaConsumerConfigManager.refreshConsumerConfig(true,getCallerConfigMap(configs));
        log.info("MappingConsumeRuleLoader loadOnTime success,permitConfigs.size={}", configs.size());
    }

    private Map<String, MetaConsumer> getCallerConfigMap(List<MetaConsumer> metaCallerConfigs) {
        Map<String, MetaConsumer> map = new ConcurrentHashMap<>();
        metaCallerConfigs.forEach(p->{
            map.put(p.getConsumerGroupName(), p);
        });
        return map;
    }

}
