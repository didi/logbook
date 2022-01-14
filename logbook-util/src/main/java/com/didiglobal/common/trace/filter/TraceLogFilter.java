package com.didiglobal.common.trace.filter;


import com.didiglobal.common.trace.info.InterfaceInfo;
import com.didiglobal.common.trace.info.TraceConstants;
import com.didiglobal.common.trace.info.TraceLog;
import com.didiglobal.common.trace.TraceUtil;
import com.didiglobal.common.web.WebUtils;
import com.didiglobal.common.trace.info.HttpServiceTraceInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * 打 http server tracelog
 */
public class TraceLogFilter extends GatherTraceInfoFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceLogFilter.class);

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static final String HTTP_REQUEST_INPUT_KEY = "trace.http.info.input.body";

    public static final String HTTP_RESPONSE_OUTPUT_KEY = "trace.http.info.output.body";

    public static final String HEADER_RID = "header-rid";

    public static final String HEADER_SPANID = "header-spanid";

    public static final String DEFAULT_SERVICE_NAME = "Http-Service";

    /**
     * 入口
     *
     * @param request
     * @param response
     * @param serviceName
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onPreHandle(ServletRequest request,
                                  ServletResponse response,
                                  Object serviceName) throws Exception {
        // 收集信息
        super.onPreHandle(request, response, serviceName);

        // ThreadLocal 设置 traceEntry
        setupTraceInfo(WebUtils.toHttp(request), WebUtils.toHttp(response), serviceName);

        TraceLog log = genRequestInLog(request, response, serviceName);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TraceLog: {}", log);
        }

        TraceUtil.print(log, !log.success);

        return true;
    }

    /**
     * 出口
     *
     * @param request
     * @param response
     * @param exception
     * @throws Exception
     */
    @Override
    public void afterCompletion(ServletRequest request,
                                ServletResponse response,
                                Exception exception) throws Exception {
        super.afterCompletion(request, response, exception);

        TraceLog log = genRequestOutLog(request, response, exception);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TraceLog: {}", log);
        }

        TraceUtil.print(log, !log.success);

        TraceUtil.removeTraceEntry();
    }

    protected void setupTraceInfo(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Object serviceName) {

        TraceUtil.ensureTraceId(request.getHeader(HEADER_RID));
        TraceUtil.ensureSpanId(request.getHeader(HEADER_SPANID));

        String serviceNameStr = null == serviceName ? DEFAULT_SERVICE_NAME : String.valueOf(serviceName);
        HttpServiceTraceInfo httpTraceInfo = getHttpTraceInfo(request);
        InterfaceInfo interfaceInfo = new InterfaceInfo(
                httpTraceInfo.getUri(),
                defaultIfBlank(serviceNameStr, DEFAULT_SERVICE_NAME),
                new Object[]{httpTraceInfo.getRequestHeaders(), httpTraceInfo.getRequestBody()}
        );
        TraceUtil.getTraceEntry().setInterfaceInfo(interfaceInfo);
    }

    protected String ensureRequestInput(ServletRequest request,
                                        HttpServiceTraceInfo httpTraceInfo) {
        Object input = request.getAttribute(HTTP_REQUEST_INPUT_KEY);
        if (input == null) {
            try {
                input = MAPPER.writeValueAsString(httpTraceInfo.getRequest());
                request.setAttribute(HTTP_REQUEST_INPUT_KEY, input);
            } catch (Exception e) {
                LOGGER.warn("caught error when set trace.http.info.input.body:", e);
            }
        }
        return (String) input;
    }

    protected String ensureResponseOutput(ServletRequest request,
                                          HttpServiceTraceInfo httpTraceInfo) {
        Object output = request.getAttribute(HTTP_RESPONSE_OUTPUT_KEY);
        if (output == null) {
            try {
                output = MAPPER.writeValueAsString(httpTraceInfo.getResponse());
                request.setAttribute(HTTP_RESPONSE_OUTPUT_KEY, output);
            } catch (Exception e) {
                LOGGER.warn("caught error when set trace.http.info.output.body:", e);
            }
        }
        return (String) output;
    }

    protected TraceLog genRequestInLog(ServletRequest request,
                                       ServletResponse response,
                                       Object mappedValue) {
        TraceLog log = new TraceLog();
        HttpServiceTraceInfo httpTraceInfo = getHttpTraceInfo(request);

        log.setInterfaceName(httpTraceInfo.getUri());
        log.setHeaders(httpTraceInfo.getRequestHeaders());
        log.setRemoteIp(httpTraceInfo.getRemoteAddress());
        log.setClientIp(httpTraceInfo.getLocalAddress());
        log.setSuccess(true);
        log.setTimestamp(httpTraceInfo.getStartTime());
        log.setRpcMethodArgs(ensureRequestInput(request, httpTraceInfo));
        log.setTag(TraceConstants._COM_REQUEST_IN);

        return log;
    }

    protected TraceLog genRequestOutLog(ServletRequest request,
                                        ServletResponse response,
                                        Exception exception) {
        TraceLog log = new TraceLog();

        HttpServiceTraceInfo httpTraceInfo = getHttpTraceInfo(request);

        log.setInterfaceName(httpTraceInfo.getUri());
        log.setCost((int) (getProcessFinishTime(request) - getProcessStartTime(request)));
        log.setHeaders(httpTraceInfo.getRequestHeaders());
        log.setRemoteIp(httpTraceInfo.getRemoteAddress());
        log.setClientIp(httpTraceInfo.getLocalAddress());
        log.setFailureException(exception);
        log.setSuccess(null == exception);
        log.setTimestamp(httpTraceInfo.getFinishTime());
        log.setRpcMethodArgs(ensureRequestInput(request, httpTraceInfo));
        log.setRpcMethodResult(ensureResponseOutput(request, httpTraceInfo));
        log.setTag(TraceConstants._COM_REQUEST_OUT);

        return log;
    }

}
