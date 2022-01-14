package com.didiglobal.api.biz.service;

import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.param.*;
import com.didiglobal.common.pojo.vo.*;

import java.util.List;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
public interface BizEventService {
    /**
     * 业务事件 准入
     * @param eventParam
     * @return
     */
    PermitVo<Object> permit(CallerInfo callerInfo, BizEventParam eventParam);

    /**
     * 业务事件 翻译
     * @param eventParam
     * @return
     */
    TranslateVo translate(CallerInfo callerInfo, BizEventParam eventParam);


    /**
     * 业务事件 上报（含准入+翻译逻辑）
     *
     * @param callerInfo
     * @param eventParam
     * @return
     */
    ReportVo<Object> report(CallerInfo callerInfo, BizEventParam eventParam);

    /**
     * 重试 上报
     *
     * @param callerInfo
     * @param param
     * @return
     */
    ReportVo<Object> reportRetry(CallerInfo callerInfo, RetryReportParam param);

}
