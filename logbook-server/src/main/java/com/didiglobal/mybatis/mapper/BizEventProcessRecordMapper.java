package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.BizEventProcessRecord;
import com.didiglobal.mybatis.provider.BizEventProcessRecordProvider;
import org.apache.ibatis.annotations.*;

/**
 * @author mayingdong
 * @date 2020/11/30
 */
@Mapper
public interface BizEventProcessRecordMapper {
    /**
     * 查询
     * @param globalBizEventId
     * @return BizEventProcessRecord
     */
    @Select("SELECT * FROM record_biz_event_progress WHERE global_biz_event_id=#{globalBizEventId}")
    BizEventProcessRecord queryByBizEventId(String globalBizEventId);

    /**
     * 插入
     * @param record
     * @return int
     */
    @InsertProvider(type = BizEventProcessRecordProvider.class, method = "insert")
    int insert(BizEventProcessRecord record);

    /**
     * 更新数据
     * @param record
     * @return int
     */
    @UpdateProvider(type = BizEventProcessRecordProvider.class, method = "update")
    int update(@Param("record") BizEventProcessRecord record, @Param("oldStatus")int oldStatus);

}
