package com.didiglobal.common.trace.filter;


import com.didiglobal.common.web.WebUtils;
import com.didiglobal.common.trace.info.HttpServiceTraceInfo;
import com.didiglobal.common.web.wrapper.ContentCachingRequestWrapper;
import com.didiglobal.common.web.wrapper.ContentCachingResponseWrapper;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// https://english.stackexchange.com/questions/188743/what-is-the-difference-in-meaning-and-usage-between-gather-and-collect

/**
 * 进行必要的信息收集（HttpServiceTraceInfo）
 * - 写入到 request 的 "trace.http.info" 属性里
 * - 可以用 getHttpTraceInfo() 方法读取
 *
 * 收集来的信息具体怎么用由子类决定
 */
public abstract class GatherTraceInfoFilter extends ProcessTimeFilter {

    public static final String HTTP_TRACE_INFO_KEY = "trace.http.info";

    protected HttpServiceTraceInfo getHttpTraceInfo(ServletRequest request) {
        return (HttpServiceTraceInfo) request.getAttribute(HTTP_TRACE_INFO_KEY);
    }

    @Override
    public void doFilterInternal(ServletRequest request,
                                 ServletResponse response,
                                 FilterChain chain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(WebUtils.toHttp(request));
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(WebUtils.toHttp(response));

        super.doFilterInternal(wrappedRequest, wrappedResponse, chain);
    }

    @Override
    protected boolean onPreHandle(ServletRequest request,
                                  ServletResponse response,
                                  Object mappedValue) throws Exception {
        super.onPreHandle(request, response, mappedValue);

        if (request.getAttribute(HTTP_TRACE_INFO_KEY) != null) {
            return true;
        }

        HttpServletRequest servletRequest = WebUtils.toHttp(request);

        HttpServiceTraceInfo httpTraceInfo = HttpServiceTraceInfo.makeDefault()
                .setMethod(servletRequest.getMethod())
                .setUri(WebUtils.getRequestUri(servletRequest))
                .setQuery(servletRequest.getQueryString())
                .setRequestHeaders(WebUtils.getRequestHeaders(servletRequest))
                .setRequestBody(WebUtils.getRequestBody(servletRequest))
                .setRemoteAddress(servletRequest.getRemoteAddr())
                .setLocalAddress(servletRequest.getLocalAddr())
                .setStartTime(getProcessStartTime(request));

        request.setAttribute(HTTP_TRACE_INFO_KEY, httpTraceInfo);

        return true;
    }

    @Override
    public void afterCompletion(ServletRequest request,
                                ServletResponse response,
                                Exception exception) throws Exception {
        super.afterCompletion(request, response, exception);

        HttpServletResponse servletResponse = WebUtils.toHttp(response);
        HttpServiceTraceInfo httpTraceInfo = (HttpServiceTraceInfo) request
                .getAttribute(HTTP_TRACE_INFO_KEY);

        httpTraceInfo.setFinishTime(getProcessFinishTime(request))
                .setResponseHeaders(WebUtils.getResponseHeaders(servletResponse))
                .setResponseBody(WebUtils.getResponseBody(servletResponse))
                .setResponseStatus(servletResponse.getStatus());

        flushRawResponse(response);
    }

    protected void flushRawResponse(ServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper;
        if (response instanceof ContentCachingResponseWrapper) {
            responseWrapper = (ContentCachingResponseWrapper) response;
        } else {
            responseWrapper = new ContentCachingResponseWrapper(WebUtils.toHttp(response));
        }
        responseWrapper.copyBodyToResponse();
    }
}
