package com.didiglobal.api.biz.controller;

import com.didiglobal.api.biz.service.BizEventService;
import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.authz.resolver.ApiCaller;
import com.didiglobal.common.param.BizEventParam;
import com.didiglobal.common.pojo.vo.PermitVo;
import com.didiglobal.common.pojo.vo.ReportVo;
import com.didiglobal.common.pojo.vo.TranslateAndSyncResult;
import com.didiglobal.common.pojo.vo.TranslateVo;
import com.didiglobal.common.response.BaseResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@RestController
@RequestMapping(value = {"/api/event"})
public class BizEventController {


    @Resource
    BizEventService bizEventService;

    /**
     * 业务事件 准入
     *
     * @param eventParam
     * @return
     */
    @RequestMapping(value = "/permit", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<PermitVo<Object>> permit(@ApiCaller CallerInfo callerInfo,
                                                 @RequestBody @Validated BizEventParam eventParam) {
        PermitVo<Object> vo = bizEventService.permit(callerInfo, eventParam);
        return BaseResponse.make(vo);
    }

    /**
     * 业务事件 翻译
     *
     * @param eventParam
     * @return
     */
    @RequestMapping(value = "/translate", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<TranslateVo> translate(@ApiCaller CallerInfo callerInfo,
                                               @RequestBody @Validated BizEventParam eventParam) {
        TranslateVo vo = bizEventService.translate(callerInfo, eventParam);
        return BaseResponse.make(vo);
    }

    /**
     * 业务事件 上报（含准入+翻译逻辑）
     *
     * @param eventParam
     * @return
     */
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<ReportVo<Object>> report(@ApiCaller CallerInfo callerInfo,
                                                 @RequestBody @Validated BizEventParam eventParam) {
        ReportVo<Object> vo = bizEventService.report(callerInfo, eventParam);
        return BaseResponse.make(vo);
    }




}
