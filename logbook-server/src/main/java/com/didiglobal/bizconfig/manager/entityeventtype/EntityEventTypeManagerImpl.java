package com.didiglobal.bizconfig.manager.entityeventtype;

import com.didiglobal.mybatis.entity.MetaEntityEventType;
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
public class EntityEventTypeManagerImpl implements EntityEventTypeManager{
    private Map<Long, MetaEntityEventType> entityEventTypeMap;

    public EntityEventTypeManagerImpl() {
        entityEventTypeMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isExist(Long entityEventTypeId) {
        return entityEventTypeMap.containsKey(entityEventTypeId);
    }

    @Override
    public Long getEntityTypeId(Long entityEventTypeId) {
        return isExist(entityEventTypeId)?entityEventTypeMap.get(entityEventTypeId).getEntityTypeId():null;
    }

    @Override
    public void refreshEntityEventTypeMap(Map<Long, MetaEntityEventType> map) {
        this.entityEventTypeMap = map;
        log.info("EntityEventTypeManagerImpl refresh success,size={}",this.entityEventTypeMap.size());
    }
}
