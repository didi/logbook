package com.didiglobal.api.alive;

import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.authz.resolver.ApiCaller;
import com.didiglobal.common.response.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liyanling
 * @date 2021/10/26
 */
@RestController
@RequestMapping(value = {"/alive"})
@SuppressWarnings("unchecked")
public class AliveController {

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<String> aliveStatus(@ApiCaller CallerInfo callerInfo) {
        return BaseResponse.make("hi "+callerInfo);
    }

}
