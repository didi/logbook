package com.didiglobal.common.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public abstract class OncePerRequestFilter extends NameableFilter {

    private static final Logger log = LoggerFactory.getLogger(OncePerRequestFilter.class);

    public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public final void doFilter(ServletRequest request, ServletResponse response,
                               FilterChain filterChain)
            throws ServletException, IOException {
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        if (request.getAttribute(alreadyFilteredAttributeName) != null) {
            // 如果是待后缀的，doFilter 找下一个执行
            filterChain.doFilter(request, response);
        } else if (!isEnabled(request, response)) {
            filterChain.doFilter(request, response);
        } else {
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
            try {
                doFilterInternal(request, response, filterChain);
            } finally {
                request.removeAttribute(alreadyFilteredAttributeName);
            }
        }
    }

    /**
     * filter对某个request是否启用
     */
    protected boolean isEnabled(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        return isEnabled();
    }

    // 获取当前 filter 的名字，加上后缀 .FILTERED
    protected String getAlreadyFilteredAttributeName() {
        String name = getName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ALREADY_FILTERED_SUFFIX;
    }

    protected abstract void doFilterInternal(ServletRequest request, ServletResponse response,
                                             FilterChain chain)
            throws ServletException, IOException;
}