package com.didiglobal.mediator.storage;

import com.didiglobal.mybatis.entity.BizEventProcessRecord;


/**
 * 用于 BizEventProcessRecord 增删改查的各种接口
 *
 * @author mayingdong
 * @date 2020/11/30
 */
public interface BizEventRecordStorage {

    /**
     * 插入数据
     * @param record
     * @return
     */
    int insert(BizEventProcessRecord record);

    /**
     * 更新数据
     * @param record
     * @return
     */
    int update(BizEventProcessRecord record,int expectedStatus);

    /**
     * 根据 bizEventId 查询数据
     * @param bizEventId
     * @return
     */
    BizEventProcessRecord queryByBizEventId(String bizEventId);

//    /**
//     * 根据时间等参数，对查询未完成上报的数据
//     * @param fixTime
//     * @return
//     */
//    List<BizEventProcessRecord> queryUnReportEventByCreateTime(FixTimeParam fixTime);
}
