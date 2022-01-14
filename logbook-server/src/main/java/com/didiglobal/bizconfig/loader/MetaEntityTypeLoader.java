package com.didiglobal.bizconfig.loader;

import com.didiglobal.bizconfig.manager.entitytype.EntityTypeManager;
import com.didiglobal.mybatis.entity.MetaEntityType;
import com.didiglobal.mybatis.mapper.MetaEntityTypeMapper;
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
public class MetaEntityTypeLoader {

    @Resource
    EntityTypeManager entityTypeManager;

    @Resource
    MetaEntityTypeMapper metaEntityTypeMapper;


    /**
     * 启动时加载
     */
    @PostConstruct
    public void loadOnStartup() {
        List<MetaEntityType> configs = metaEntityTypeMapper.queryAllValidEntityType();
        entityTypeManager.refreshEntityTypeMap(getConfigMap(configs));
        log.info("EntityTypeLoader loadOnStartup success, configs.size={}", configs.size());
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${meta_entity_type_load_cron}")
    public synchronized void loadOnTime() {
        log.info("EntityTypeLoader loadOnTime start");
        List<MetaEntityType> configs = metaEntityTypeMapper.queryAllValidEntityType();
        if(configs == null || configs.isEmpty()) {
            log.info("EntityTypeLoader loadOnTime break,permitConfigs.size=0");
            return;
        }
        entityTypeManager.refreshEntityTypeMap(getConfigMap(configs));
        log.info("EntityTypeLoader loadOnTime success,permitConfigs.size={}", configs.size());
    }

    private Map<Long, MetaEntityType> getConfigMap(List<MetaEntityType> configs) {
        Map<Long, MetaEntityType> map = new ConcurrentHashMap<>();
        configs.forEach(p->{
            map.put(p.getId(), p);
        });
        return map;
    }

}
