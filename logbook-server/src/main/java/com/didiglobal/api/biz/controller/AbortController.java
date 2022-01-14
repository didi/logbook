package com.didiglobal.api.biz.controller;

import com.didiglobal.api.biz.service.AbortService;
import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.authz.resolver.ApiCaller;
import com.didiglobal.common.param.BizEventProcessAbortParam;
import com.didiglobal.common.param.EntityEventProcessAbortParam;
import com.didiglobal.common.pojo.vo.AbortVo;
import com.didiglobal.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyanling
 * @date 2021/11/23 10:48 下午
 */
@RestController
@RequestMapping(value = {"/api/abort"})
public class AbortController {

    @Autowired
    AbortService abortService;

    /**
     * 业务事件处理流程（翻译）中断
     */
    @RequestMapping(value = "/biz_event_progress", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<AbortVo<Object>> bizEventProgressAbort(
            @ApiCaller CallerInfo callerInfo, @RequestBody @Validated BizEventProcessAbortParam param) {
        AbortVo<Object> vo = abortService.bizEventProgressAbort(callerInfo, param);
        return BaseResponse.make(vo);
    }

    /**
     * 实体事件处理流程（分发）中断
     */
    @RequestMapping(value = "/entity_event_progress", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<AbortVo<Object>> entityEventProgressAbort(
            @ApiCaller CallerInfo callerInfo, @RequestBody @Validated EntityEventProcessAbortParam param) {
        AbortVo<Object> vo = abortService.entityEventProgressAbort(callerInfo, param);
        return BaseResponse.make(vo);
    }

}
