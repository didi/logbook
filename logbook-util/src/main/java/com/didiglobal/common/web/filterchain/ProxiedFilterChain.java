package com.didiglobal.common.web.filterchain;


import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * FilterChain 的代理类
 * 效果：在执行 orig.doFilter() 前，先执行 filters 中 Filter 的 doFilter()
 */
public class ProxiedFilterChain implements FilterChain {

    private FilterChain orig;
    private List<Filter> filters;
    private int index = 0;

    public ProxiedFilterChain(FilterChain orig, List<Filter> filters) {
        if (orig == null) {
            throw new NullPointerException("original FilterChain cannot be null.");
        }
        this.orig = orig;
        this.filters = filters;
        this.index = 0;
    }

    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (this.filters == null || this.filters.size() == this.index) {
            this.orig.doFilter(request, response);
        } else {
            this.filters.get(this.index++).doFilter(request, response, this);
        }
    }
}
