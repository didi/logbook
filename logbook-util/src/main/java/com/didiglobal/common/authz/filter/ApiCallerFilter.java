package com.didiglobal.common.authz.filter;

import com.didiglobal.common.authz.CallerAuthConstant;
import com.didiglobal.common.authz.CallerInfo;
import com.didiglobal.common.error.AuthError;
import com.didiglobal.common.error.CommonError;
import com.didiglobal.common.message.MessageManager;
import com.didiglobal.common.response.BaseResponse;
import com.didiglobal.common.trace.filter.TraceLogFilter;
import com.didiglobal.common.web.WebUtils;
import com.didiglobal.common.web.filter.PathMatchingFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * @author liyanling
 * @date 2021/10/29
 */
public class ApiCallerFilter extends PathMatchingFilter {

    //    private static final ReportLogger LOGGER = LoggerFactory.getLogger(ApiCallerFilter.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(TraceLogFilter.class);

    private static final ContentType APPLICATION_JSON = ContentType.create("application/json", Consts.UTF_8);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_HEADER_CALLER_KEY = "x-caller";
    private static final String DEFAULT_HEADER_AUTHORIZATION_KEY = "x-authorization";
    private static final String DEFAULT_RESPONSE_TEMPLATE = "{\"errno\": %s, \"errmsg\": \"%s\"}";
    private BaseResponse DEFAULT_ERROR = BaseResponse.makeError(AuthError.AUTH_SYSTEM_ERROR);//非预期的系统异常
    private BaseResponse NEED_AUTHZ_ERROR = BaseResponse.makeError(AuthError.NEED_AUTHZ);//没有提供有效的鉴权参数
    private BaseResponse ACCESS_DENY_ERROR = BaseResponse.makeError(AuthError.ACCESS_DENY);//没有找到 caller 对应的配置
    private BaseResponse AUTHZ_FAILED_ERROR = BaseResponse.makeError(AuthError.AUTHZ_FAILED);//authorization 对比校验失败


    // 1. 用于读取 caller 对应的配置
    private CallerConfigProvider configProvider;

    // 2. 用于生成或验证 authorization
    private AuthorizationProcessor authorizationProcessor;

    // 3. 用于映射文案
    private MessageManager messageMgr;

    // 4. 用于写返回值
    private String responseTemplate = DEFAULT_RESPONSE_TEMPLATE;

    // 5. header参数
    private String xCaller = DEFAULT_HEADER_CALLER_KEY;
    private String xAuthorization = DEFAULT_HEADER_AUTHORIZATION_KEY;

    private ApiCallerFilter(CallerConfigProvider configProvider, AuthorizationProcessor authorizationProcessor,
                            MessageManager messageMgr, String responseTemplate, String xcaller, String xauthorization) {
        this.configProvider = configProvider;
        this.authorizationProcessor = authorizationProcessor;
        this.messageMgr = messageMgr;
        this.responseTemplate = responseTemplate;
        this.xCaller = xcaller;
        this.xAuthorization = xauthorization;
    }

    /**
     * 重写 onPreHandle 进行鉴权
     *
     * @param servletRequest
     * @param servletResponse
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onPreHandle(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue)
            throws Exception {
        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        HttpServletResponse response = WebUtils.toHttp(servletResponse);

        // 校验
        BaseResponse<CallerInfo> validateResult = validate(request);

        // 若失败，回写结果
        if (!isSuccess(validateResult)) {
            processFailedResponse(response, validateResult);
            return false;
        }

        // 若成功，塞 attribute
        setCallerContext(request, validateResult.getData());
        return true;
    }


    private void setCallerContext(HttpServletRequest request, CallerInfo caller) {
        request.setAttribute(CallerAuthConstant.API_CALLER_INFO, caller);
    }


    private BaseResponse<CallerInfo> validate(HttpServletRequest request) {
        String caller = request.getHeader(xCaller);
        String authorization = request.getHeader(xAuthorization);

        // 1. 参数检查
        if (!checkParam(caller, authorization)) {
            LOGGER.warn("param error,caller={},authorization={}", caller, authorization);
            return NEED_AUTHZ_ERROR;
        }

        // 2. 校验并获得 CallerInfo，若抛异常则表示没配置
        CallerInfo callerInfo = null;
        try {
            callerInfo = authorizationProcessor.validate(caller, configProvider, authorization);
        } catch (Exception e) {
            LOGGER.warn("getCallerConfig failed,caller={},e=", caller, e);
            return ACCESS_DENY_ERROR;
        }

        // 3. 若caller为null表示校验失败
        if (null == callerInfo) {
            LOGGER.warn("authorization check failed,caller={},authorization={}", caller, authorization);
            return AUTHZ_FAILED_ERROR;
        }

        return BaseResponse.make(callerInfo);
    }


    // 参数检查
    protected boolean checkParam(String caller, String authorization) {
        return !StringUtils.isAnyBlank(caller, authorization);
    }

    // 判断是否鉴权通过
    private boolean isSuccess(BaseResponse<CallerInfo> response) {
        if (null == response) {
            return false;
        }
        return response.getErrno() == CommonError.SUCCESS.getErrno();
    }

    // 鉴权失败的处理逻辑：获取文案，write 回写
    protected void processFailedResponse(ServletResponse response, BaseResponse result) {
        result = null == result ? DEFAULT_ERROR : result;

        int errno = result.getErrno();
        String msg = getResponseMsg(result.getErrmsg());
        response.setContentType(APPLICATION_JSON.toString());
        response.setCharacterEncoding(APPLICATION_JSON.getCharset().name());

        try {
            String responseData = String.format(responseTemplate, errno, msg);
            WebUtils.toHttp(response).getOutputStream().write(responseData.getBytes(DEFAULT_CHARSET));
        } catch (Throwable t) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn("caught error when process open api failed response|error=", t);
            }
        }
    }


    // 映射文案
    protected String getResponseMsg(String errmsg) {
        if (null == messageMgr) {
            return defaultIfBlank(errmsg, EMPTY);
        }
        if (StringUtils.isBlank(errmsg)) {
            return EMPTY;
        }
        String msg = messageMgr.getMessage(errmsg);
        msg = StringUtils.equals(msg, errmsg) ? errmsg : msg;
        if (StringUtils.isNotBlank(msg)) {
            return msg;
        }
        return messageMgr.getMessage(errmsg);
    }


    // Builder


    public static Builder custom(CallerConfigProvider callerConfigProvider) {
        return new Builder(callerConfigProvider);
    }

    public static class Builder {
        CallerConfigProvider callerConfigProvider;
        AuthorizationProcessor authorizationProcessor = new Md5AuthorizationProcessor();
        MessageManager messageManager = MessageManager.INSTANCE;
        String responseTemplate = DEFAULT_RESPONSE_TEMPLATE;
        String headerCallerKey = DEFAULT_HEADER_CALLER_KEY;
        String headerAuthorizationKey = DEFAULT_HEADER_AUTHORIZATION_KEY;

        public Builder(CallerConfigProvider callerConfigProvider) {
            this.callerConfigProvider = callerConfigProvider;
        }

        public Builder setCallerConfigProvider(CallerConfigProvider callerConfigProvider) {
            this.callerConfigProvider = callerConfigProvider;
            return this;
        }

        public Builder setAuthorizationProcessor(AuthorizationProcessor authorizationProcessor) {
            this.authorizationProcessor = authorizationProcessor;
            return this;
        }

        public Builder setMessageManager(MessageManager messageManager) {
            this.messageManager = messageManager;
            return this;
        }

        public Builder setResponseTemplate(String responseTemplate) {
            this.responseTemplate = responseTemplate;
            return this;
        }

        public Builder setHeaderCallerKey(String headerCallerKey) {
            this.headerCallerKey = headerCallerKey;
            return this;
        }

        public Builder setHeaderAuthorizationKey(String headerAuthorizationKey) {
            this.headerAuthorizationKey = headerAuthorizationKey;
            return this;
        }


        public ApiCallerFilter build() {
            return new ApiCallerFilter(callerConfigProvider, authorizationProcessor, messageManager,
                    responseTemplate, headerCallerKey, headerAuthorizationKey);
        }
    }

}
