package com.didiglobal.common.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class AbstractFilter extends ServletContextSupport implements Filter {

    private static transient final Logger log = LoggerFactory.getLogger(AbstractFilter.class);

    /**
     * servlet容器启动时设置
     */
    protected FilterConfig filterConfig;

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        setServletContext(filterConfig.getServletContext());
    }


    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        setFilterConfig(filterConfig);
        try {
            onFilterConfigSet();
        } catch (Exception e) {
            if (e instanceof ServletException) {
                throw (ServletException) e;
            } else {
                log.error("Unable to start Filter: [" + e.getMessage() + "].", e);
                throw new ServletException(e);
            }
        }
    }

    protected void onFilterConfigSet() throws Exception {
    }

    @Override
    public void destroy() {
    }
}