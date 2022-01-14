package com.didiglobal.common.web.filterchain.resolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 作用：为当前请求（request） 匹配一个 filterChain（一组有序的 filter）
 * 即：通过代码逻辑把一个 filterChain 应用到任何 request 或 uri 上
 *
 * <p>
 * 这个机制可以解决在web.xml配一坨filter、在spring配置文件里配一拖拦截器、spring java config配置webfilter的繁杂、不直观的问题
 * 可读性更好，同时也让应用有了在运行时动态变更filter的能力
 */
public interface FilterChainResolver {

    /**
     * 获取 filterchain（实际上 originalChain 的代理）
     * 如果没有合适的链可用，直接返回原始链
     */
    FilterChain getChain(ServletRequest request, ServletResponse response,FilterChain originalChain);

}
