package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.caller.CallerConfigManager;
import com.didiglobal.mybatis.entity.MetaCaller;
import com.didiglobal.mybatis.mapper.MetaCallerMapper;
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
public class MetaCallerConfigLoader {

    @Autowired
    CallerConfigManager callerConfigManager;

    @Resource
    MetaCallerMapper metaCallerMapper;


    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<MetaCaller> configs = metaCallerMapper.queryAllValidCaller();
        callerConfigManager.refreshCallerConfigMap(getCallerConfigMap(configs));
        log.info("CallerConfigLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${meta_caller_load_cron}")
    public synchronized void loadOnTime() {
        log.info("CallerConfigLoader loadOnTime start");
        List<MetaCaller> configs = metaCallerMapper.queryAllValidCaller();
        if(configs == null || configs.isEmpty()) {
            log.info("CallerConfigLoader loadOnTime break,permitConfigs.size=0");
            return;
        }
        callerConfigManager.refreshCallerConfigMap(getCallerConfigMap(configs));
        log.info("CallerConfigLoader loadOnTime success,permitConfigs.size={}", configs.size());
    }

    private Map<String, MetaCaller> getCallerConfigMap(List<MetaCaller> metaCallerConfigs) {
        Map<String, MetaCaller> map = new ConcurrentHashMap<>();
        metaCallerConfigs.forEach(p->{
            map.put(p.getCallerName(), p);
        });
        return map;
    }

}
