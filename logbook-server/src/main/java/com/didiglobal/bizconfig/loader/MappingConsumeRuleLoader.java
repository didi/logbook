package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.consumerule.MappingConsumeRuleManager;
import com.didiglobal.mybatis.entity.MappingConsumeRule;
import com.didiglobal.mybatis.mapper.MappingConsumeRuleMapper;
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
@Slf4j
@Component
@EnableScheduling
public class MappingConsumeRuleLoader {

    @Resource
    MappingConsumeRuleMapper mappingConsumeRuleMapper;

    @Resource
    MappingConsumeRuleManager mappingConsumeRuleManager;


    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<MappingConsumeRule> configs = mappingConsumeRuleMapper.queryValidConsumeRule();
        mappingConsumeRuleManager.refreshConsumeRuleMap(getCallerConfigMap(configs));
        log.info("MappingConsumeRuleLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${mapping_consume_rule_load_cron}")
    public synchronized void loadOnTime() {
        log.info("MappingConsumeRuleLoader loadOnTime start");
        List<MappingConsumeRule> configs = mappingConsumeRuleMapper.queryValidConsumeRule();
        if(configs == null || configs.isEmpty()) {
            log.info("MappingConsumeRuleLoader loadOnTime break,permitConfigs.size=0");
            return;
        }
        mappingConsumeRuleManager.refreshConsumeRuleMap(getCallerConfigMap(configs));
        log.info("MappingConsumeRuleLoader loadOnTime success,permitConfigs.size={}", configs.size());
    }

    private Map<String, MappingConsumeRule> getCallerConfigMap(List<MappingConsumeRule> metaCallerConfigs) {
        Map<String, MappingConsumeRule> map = new ConcurrentHashMap<>();
        metaCallerConfigs.forEach(p->{
            map.put(p.getConsumerGroupName(), p);
        });
        return map;
    }

}
