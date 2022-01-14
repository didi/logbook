package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.EntityEventDispatchRecord;
import com.didiglobal.mybatis.provider.EntityEventDispatchRecordProvider;
import org.apache.ibatis.annotations.*;

/**
 * @author mayingdong
 * @date 2020/11/30
 */
@Mapper
public interface EntityEventDispatchRecordMapper {
    // 查询
    @Select("SELECT * FROM record_entity_event_progress WHERE consume_global_event_id= #{consumeGlobalEventId}")
    EntityEventDispatchRecord queryByConsumeGlobalEventId(String consumeGlobalEventId);

    // 插入
    @InsertProvider(type = EntityEventDispatchRecordProvider.class, method = "insert")
    int insert(EntityEventDispatchRecord record);

    // 更新数据
    @UpdateProvider(type = EntityEventDispatchRecordProvider.class, method = "update")
    int update(@Param("record")EntityEventDispatchRecord record, @Param("oldStatus")int oldStatus);
}
