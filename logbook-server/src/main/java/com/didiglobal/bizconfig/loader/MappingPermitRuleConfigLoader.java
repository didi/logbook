package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.permit.PermitRuleConfigManager;
import com.didiglobal.mybatis.entity.PermitRuleConfig;
import com.didiglobal.mybatis.mapper.MappingPermitConfigMapper;
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
public class MappingPermitRuleConfigLoader {

    @Resource
    MappingPermitConfigMapper permitConfigMapper;

    @Resource
    PermitRuleConfigManager permitRuleConfigManager;

    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<PermitRuleConfig> permitConfigs = permitConfigMapper.queryAllValidPermitConfig();
        permitRuleConfigManager.refreshPermitRuleConfigMap(getConfigMap(permitConfigs));
        log.info("PermitRuleConfigLoader loadOnStartup success, permitConfigs.size={}", permitConfigs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${mapping_permit_load_cron}")
    public synchronized void loadOnTime() {
        log.info("PermitRuleConfigLoader loadOnTime start");
        List<PermitRuleConfig> configs = permitConfigMapper.queryAllValidPermitConfig();
        if(configs == null || configs.isEmpty()) {
            log.info("PermitRuleConfigLoader loadOnTime break,configs=0");
            return;
        }
        permitRuleConfigManager.refreshPermitRuleConfigMap(getConfigMap(configs));
        log.info("PermitRuleConfigLoader loadOnTime success,configs.size={}", configs.size());
    }

    private Map<Long, PermitRuleConfig> getConfigMap(List<PermitRuleConfig> permitRuleConfigs) {
        Map<Long, PermitRuleConfig> map = new ConcurrentHashMap<>();
        permitRuleConfigs.forEach(p->{
            map.put(p.getBizEventTypeId(), p);
        });
        return map;
    }

}
