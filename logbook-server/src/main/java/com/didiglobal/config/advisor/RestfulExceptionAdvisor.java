package com.didiglobal.config.advisor;

import com.didiglobal.common.LogbookError;
import com.didiglobal.common.LogbookException;
import com.didiglobal.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyanling
 * @date 2021/10/26
 * convert all Exception to unified format: BaseResponse
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestfulExceptionAdvisor {

    @ExceptionHandler(LogbookException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse handleException(LogbookException e) {
        log.warn("handleException LogbookException|e=", e);
        return BaseResponse.makeErrorWithData(e,e.getExceptionDetail());
    }

    // Exception as UNEXPECTED_ERROR
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse handleException(Exception e) {
        log.error("handleException Exception|e=", e);
        return BaseResponse.makeErrorWithData(LogbookError.UNEXPECTED_ERROR,e.getMessage());
    }
}
