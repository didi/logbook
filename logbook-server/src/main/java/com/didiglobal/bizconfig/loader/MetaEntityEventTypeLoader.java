package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.entityeventtype.EntityEventTypeManager;
import com.didiglobal.mybatis.entity.MetaEntityEventType;
import com.didiglobal.mybatis.mapper.MetaEntityEventTypeMapper;
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
public class MetaEntityEventTypeLoader {

    @Resource
    EntityEventTypeManager entityEventTypeManager;

    @Resource
    MetaEntityEventTypeMapper metaEntityEventTypeMapper;


    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<MetaEntityEventType> configs = metaEntityEventTypeMapper.queryAllValidEntityEventType();
        entityEventTypeManager.refreshEntityEventTypeMap(getConfigMap(configs));
        log.info("EntityEventTypeLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${meta_entity_event_type_load_cron}")
    public synchronized void loadOnTime() {
        log.info("EntityEventTypeLoader loadOnTime start");
        List<MetaEntityEventType> configs = metaEntityEventTypeMapper.queryAllValidEntityEventType();
        if(configs == null || configs.isEmpty()) {
            log.info("EntityEventTypeLoader loadOnTime break,permitConfigs.size=0");
            return;
        }
        entityEventTypeManager.refreshEntityEventTypeMap(getConfigMap(configs));
        log.info("EntityEventTypeLoader loadOnTime success,permitConfigs.size={}", configs.size());
    }

    private Map<Long, MetaEntityEventType> getConfigMap(List<MetaEntityEventType> configs) {
        Map<Long, MetaEntityEventType> map = new ConcurrentHashMap<>();
        configs.forEach(p->{
            map.put(p.getId(), p);
        });
        return map;
    }

}
