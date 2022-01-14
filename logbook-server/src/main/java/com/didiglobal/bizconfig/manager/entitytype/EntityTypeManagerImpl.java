package com.didiglobal.bizconfig.manager.entitytype;

import com.didiglobal.mybatis.entity.MetaEntityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/17 4:18 下午
 */
@Component
@Slf4j
public class EntityTypeManagerImpl implements EntityTypeManager {
    private Map<Long, MetaEntityType> entityTypeMap;


    public EntityTypeManagerImpl() {
        this.entityTypeMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isExist(Long entityEventTypeId) {
        return entityTypeMap.containsKey(entityEventTypeId);
    }

    @Override
    public void refreshEntityTypeMap(Map<Long, MetaEntityType> map) {
        this.entityTypeMap = map;
        log.info("EntityTypeManagerImpl refresh success,size={}",this.entityTypeMap.size());
    }

}
