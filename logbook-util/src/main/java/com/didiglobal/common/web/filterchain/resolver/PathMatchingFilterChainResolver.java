package com.didiglobal.common.web.filterchain.resolver;

import com.didiglobal.common.web.WebUtils;
import com.didiglobal.common.web.filterchain.DefaultFilterChainManager;
import com.didiglobal.common.web.filterchain.FilterChainManager;
import com.didiglobal.common.web.filterchain.matcher.AntPathMatcher;
import com.didiglobal.common.web.filterchain.matcher.PatternMatcher;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class PathMatchingFilterChainResolver  implements FilterChainResolver {

    // property
    private FilterChainManager filterChainManager;

    private PatternMatcher pathMatcher;

    // constructor
    public PathMatchingFilterChainResolver() {
        this.pathMatcher = new AntPathMatcher();
        this.filterChainManager = new DefaultFilterChainManager();
    }

    public PathMatchingFilterChainResolver(FilterConfig filterConfig) {
        this.pathMatcher = new AntPathMatcher();
        this.filterChainManager = new DefaultFilterChainManager(filterConfig);
    }

    // getter and setter
    public PatternMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PatternMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public FilterChainManager getFilterChainManager() {
        return filterChainManager;
    }

    public void setFilterChainManager(FilterChainManager filterChainManager) {
        this.filterChainManager = filterChainManager;
    }

    /**
     * 找出 uri 匹配的 filterChain 并返回
     *
     * 注意：uri 按顺序匹配到任意一个 filterChainList中的 filterChain 就会直接返回，不会再匹配其他的 filterChain
     *
     * @param request
     * @param response
     * @param originalChain
     * @return
     */
    public FilterChain getChain(ServletRequest request, ServletResponse response,
                                FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        String requestURI = getPathWithinApplication(request);

        for (String pathPattern : filterChainManager.getChainNames()) {
            if (pathMatches(pathPattern, requestURI)) {
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }

        return null;
    }

    protected boolean pathMatches(String pattern, String path) {
        PatternMatcher pathMatcher = getPathMatcher();
        return pathMatcher.matches(pattern, path);
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }
}