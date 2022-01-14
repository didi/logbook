package com.didiglobal.api.biz.service.impl;

import com.didiglobal.api.biz.service.AbortService;
import com.didiglobal.common.LogbookError;
import com.didiglobal.common.LogbookException;
import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.param.BizEventProcessAbortParam;
import com.didiglobal.common.param.EntityEventProcessAbortParam;
import com.didiglobal.common.pojo.LogbookConstants;
import com.didiglobal.common.pojo.vo.AbortVo;
import com.didiglobal.mediator.storage.BizEventRecordStorage;
import com.didiglobal.mediator.storage.EntityEventDispatchRecordStorage;
import com.didiglobal.mybatis.entity.BizEventProcessRecord;
import com.didiglobal.mybatis.entity.EntityEventDispatchRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author liyanling
 * @date 2021/11/23 10:48 下午
 */
@Component
@Slf4j
public class AbortServiceImpl implements AbortService {

    // biz_event 表
    @Autowired
    BizEventRecordStorage bizEventRecordStorage;

    // dispatch 表
    @Autowired
    EntityEventDispatchRecordStorage entityEventDispatchRecordStorage;


    /**
     * 业务事件 处理流程 中断
     *
     * @param callerInfo
     * @param param
     * @return
     */
    @Override
    public AbortVo<Object> bizEventProgressAbort(CallerInfo callerInfo, BizEventProcessAbortParam param) {

        // 1. 参数检查
        checkParam(callerInfo,param);

        // 2. 查询记录
        BizEventProcessRecord recordInDb = getBizEventProcessRecordForAbort(param);

        // 3. 作废
        return doAbort(recordInDb);
    }

    /**
     * 实体事件 处理流程 中断
     *
     * @param callerInfo
     * @param param
     * @return
     */
    @Override
    public AbortVo<Object> entityEventProgressAbort(CallerInfo callerInfo, EntityEventProcessAbortParam param) {
        // 1. 参数检查
        checkParam(callerInfo,param);

        // 2. 查询记录
        EntityEventDispatchRecord recordInDb = getEntityEventDispatchRecord(param);

        // 3. 作废
        return doAbort(recordInDb);
    }




    // --------------------------------------------------------
    // param check
    // --------------------------------------------------------

    private void checkParam(CallerInfo callerInfo, BizEventProcessAbortParam param) {
        if(null == callerInfo || null == param){
            throw new LogbookException(LogbookError.PARAM_INVALID);
        }
    }

    private void checkParam(CallerInfo callerInfo, EntityEventProcessAbortParam param) {
        if(null == callerInfo || null == param){
            throw new LogbookException(LogbookError.PARAM_INVALID);
        }
    }



    // --------------------------------------------------------
    // biz_event abort
    // --------------------------------------------------------


    // 从数据库查找 BizEventProcessRecord
    private BizEventProcessRecord getBizEventProcessRecordForAbort(BizEventProcessAbortParam param) {
        BizEventProcessRecord recordInDb = bizEventRecordStorage.queryByBizEventId(param.getGlobalBizEventId());
        if(null == recordInDb){
            log.error("getBizEventProcessRecord failed,no record found");
            throw new LogbookException(LogbookError.NOT_FOUND_REPORT_RECORD);
        }
        if(recordInDb.getStatus() == LogbookConstants.BIZ_EVENT_PROCESS_COMPLETED){
            log.warn("getBizEventProcessRecord success,but record already completed");
            throw new LogbookException(LogbookError.ALREADY_REPORT_SUCCESS);
        }
        if(recordInDb.getStatus() == LogbookConstants.BIZ_EVENT_PROCESS_ABANDONED){
            log.warn("getBizEventProcessRecord success,but record already abandoned");
            throw new LogbookException(LogbookError.SUCCEED);
        }
        return recordInDb;
    }

    // 更新数据库记录 BizEventProcessRecord
    private AbortVo<Object> doAbort(BizEventProcessRecord recordInDb) {
        int oldStatus = recordInDb.getStatus();
        recordInDb.setStatus(LogbookConstants.BIZ_EVENT_PROCESS_ABANDONED);
        recordInDb.setAbortTime(new Date());
        int effect = bizEventRecordStorage.update(recordInDb,oldStatus);

        if(effect==0){
            return AbortVo.builder().success(false).result("update effect line is 0").build();
        }else{
            return AbortVo.builder().success(true).build();
        }
    }


    // --------------------------------------------------------
    // entity_event abort
    // --------------------------------------------------------


    // 从数据库查找 EntityEventDispatchRecord
    private EntityEventDispatchRecord getEntityEventDispatchRecord(EntityEventProcessAbortParam param) {
        EntityEventDispatchRecord recordInDb = entityEventDispatchRecordStorage
                .queryByConsumeGlobalEventId(param.getConsumeGlobalEventId());
        if(null == recordInDb){
            log.error("getEntityEventDispatchRecord failed,no record found");
            throw new LogbookException(LogbookError.NOT_FOUND_DISPATCH_RECORD);
        }
        if(recordInDb.getStatus() == LogbookConstants.ENTITY_EVENT_PROCESS_COMPLETED){
            log.warn("getEntityEventDispatchRecord success,but record already completed");
            throw new LogbookException(LogbookError.ALREADY_CONSUME_SUCCESS);
        }
        if(recordInDb.getStatus() == LogbookConstants.ENTITY_EVENT_PROCESS_ABANDONED){
            log.warn("getEntityEventDispatchRecord success,but record already abandoned");
            throw new LogbookException(LogbookError.SUCCEED);
        }
        return recordInDb;
    }

    // 更新数据库记录 EntityEventDispatchRecord
    private AbortVo<Object> doAbort(EntityEventDispatchRecord recordInDb) {
        int oldStatus = recordInDb.getStatus();
        recordInDb.setStatus(LogbookConstants.ENTITY_EVENT_PROCESS_ABANDONED);
        recordInDb.setAbortTime(new Date());
        int effect = entityEventDispatchRecordStorage.update(recordInDb,oldStatus);

        if(effect==0){
            return AbortVo.builder().success(false).result("update effect line is 0").build();
        }else{
            return AbortVo.builder().success(true).build();
        }
    }


}
