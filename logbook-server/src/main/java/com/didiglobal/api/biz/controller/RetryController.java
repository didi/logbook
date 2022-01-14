package com.didiglobal.api.biz.controller;

import com.didiglobal.api.biz.service.BizEventService;
import com.didiglobal.api.biz.service.ConsumerProcessor;
import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.authz.resolver.ApiCaller;
import com.didiglobal.common.param.RetryConsumeParam;
import com.didiglobal.common.param.RetryReportParam;
import com.didiglobal.common.pojo.vo.ConsumeVo;
import com.didiglobal.common.pojo.vo.ReportVo;
import com.didiglobal.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyanling
 * @date 2021/12/1 2:38 下午
 */
@RestController
@RequestMapping(value = {"/api/retry"})
public class RetryController {

    @Autowired
    ConsumerProcessor<Object> consumerProcessor;

    @Autowired
    BizEventService bizEventService;

    /**
     * 业务事件处理流程（翻译）重试
     */
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<ReportVo<Object>> reportRetry(@ApiCaller CallerInfo callerInfo,
                                                      @RequestBody @Validated RetryReportParam param) {
        ReportVo<Object> vo = bizEventService.reportRetry(callerInfo, param);
        return BaseResponse.make(vo);
    }

    /**
     * 实体事件处理流程（消费）重试
     */
    @RequestMapping(value = "/consume", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<ConsumeVo<Object>> consumeRetry(@ApiCaller CallerInfo callerInfo,
                                                        @RequestBody @Validated RetryConsumeParam param) {
        ConsumeVo<Object> vo = consumerProcessor.consumeRetry(callerInfo, param);
        return BaseResponse.make(vo);
    }

}
