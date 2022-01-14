package com.didiglobal.bizconfig.manager.entityeventtype;

import com.didiglobal.mybatis.entity.MetaEntityEventType;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/17 4:13 下午
 */
public interface EntityEventTypeManager {

    // 判断 entityEventTypeId 是否存在
    boolean isExist(Long entityEventTypeId);

    // 根据 entityEventId 取对应的 entityId
    Long getEntityTypeId(Long entityEventTypeId);

    void refreshEntityEventTypeMap(Map<Long, MetaEntityEventType> map);



}
