package com.didiglobal.api.biz.service;

import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.param.BizEventProcessAbortParam;
import com.didiglobal.common.param.EntityEventProcessAbortParam;
import com.didiglobal.common.pojo.vo.AbortVo;

/**
 * @author liyanling
 * @date 2021/11/23 10:48 下午
 */
public interface AbortService {
    /**
     * 业务事件处理流程中断
     *
     * @param callerInfo
     * @param param
     * @return
     */
    AbortVo<Object> bizEventProgressAbort(CallerInfo callerInfo, BizEventProcessAbortParam param);

    /**
     * 实体事件处理流程中断
     *
     * @param callerInfo
     * @param param
     * @return
     */
    AbortVo<Object> entityEventProgressAbort(CallerInfo callerInfo, EntityEventProcessAbortParam param);
}
