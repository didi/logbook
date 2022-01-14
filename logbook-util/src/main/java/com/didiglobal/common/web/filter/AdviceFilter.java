package com.didiglobal.common.web.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 提供 aop 风格的请求流程 hook
 */
public abstract class AdviceFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AdviceFilter.class);

    /**
     * 在执行链前调用
     * 返回true表示可以继续执行链内逻辑
     * false或抛出异常会终止链的调用
     */
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return true;
    }

    /**
     * 链执行后调用
     * 如果执行过程中抛出异常则不会调用
     */
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
    }

    /**
     * 链执行后调用
     * 无论执行过程中是否抛出异常都会调用
     * 无异常的流程里调用顺序也在post之后
     */
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception)
            throws Exception {
    }

    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain chain)
            throws Exception {
        chain.doFilter(request, response);
    }

    /**
     * 定流程
     * pre -> post -> afterCompletion
     */
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Exception exception = null;

        try {
            boolean continueChain = preHandle(request, response);

            if (continueChain) {
                executeChain(request, response, chain);
            }

            postHandle(request, response);
        } catch (Exception e) {
            exception = e;
        } finally {
            cleanup(request, response, exception);
        }
    }

    protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
            throws ServletException, IOException {
        Exception exception = existing;
        try {
            afterCompletion(request, response, exception);
        } catch (Exception e) {
            if (exception == null) {
                exception = e;
            }
        }
        if (exception != null) {
            if (exception instanceof ServletException) {
                throw (ServletException) exception;
            } else if (exception instanceof IOException) {
                throw (IOException) exception;
            } else {
                throw new ServletException(exception);
            }
        }
    }
}
