package com.didiglobal.mediator.storage;

import com.didiglobal.mybatis.entity.EntityEventDispatchRecord;

/**
 * @author liyanling
 * @date 2021/11/22 3:08 下午
 */
public interface EntityEventDispatchRecordStorage {

    int insert(EntityEventDispatchRecord record);

    int update(EntityEventDispatchRecord record,int oldStatus);

    EntityEventDispatchRecord queryByConsumeGlobalEventId(String consumeGlobalEventId);
}
