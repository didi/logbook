package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.translate.TranslateRuleConfigManager;
import com.didiglobal.mybatis.entity.TranslateRuleConfig;
import com.didiglobal.mybatis.mapper.MappingTranslateConfigMapper;
import com.google.common.collect.Lists;
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
 * @author mayingdong
 * @date 2021/11/3
 */
@Slf4j
@Component
@EnableScheduling
public class MappingTranslateRuleConfigLoader {

    @Resource
    MappingTranslateConfigMapper translateConfigMapper;

    @Resource
    TranslateRuleConfigManager translateRuleConfigManager;

    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<TranslateRuleConfig> configs = translateConfigMapper.queryAllValidTranslateConfig();
        translateRuleConfigManager.refreshBizEventTranslateRuleConfigMap(getConfigMap(configs));
        log.info("TranslateRuleConfigLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${mapping_translate_load_cron}")
    public synchronized void loadOnTime() {
        log.info("TranslateRuleConfigLoader loadOnTime start!");
        List<TranslateRuleConfig> configs = translateConfigMapper.queryAllValidTranslateConfig();
        if(configs == null || configs.isEmpty()) {
            log.info("TranslateRuleConfigLoader loadOnTime break,configs.size=0");
            return;
        }
        translateRuleConfigManager.refreshBizEventTranslateRuleConfigMap(getConfigMap(configs));
        log.info("TranslateRuleConfigLoader loadOnTime success,configs.size={}", configs.size());
    }

    private Map<Long, List<TranslateRuleConfig>> getConfigMap(List<TranslateRuleConfig> configs) {
        Map<Long, List<TranslateRuleConfig>> map = new ConcurrentHashMap<>();
        configs.forEach(p->{
            map.computeIfAbsent(p.getBizEventTypeId(), k -> Lists.newArrayList());
            List<TranslateRuleConfig> list = map.get(p.getBizEventTypeId());
            list.add(p);
        });
        return map;
    }

}
