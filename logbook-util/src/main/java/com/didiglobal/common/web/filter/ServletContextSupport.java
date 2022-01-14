package com.didiglobal.common.web.filter;

import javax.servlet.ServletContext;

public class ServletContextSupport {

    // property
    private ServletContext servletContext = null;

    // getter and setter
    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected String getContextInitParam(String paramName) {
        return getServletContext().getInitParameter(paramName);
    }

    private ServletContext getRequiredServletContext() {
        ServletContext servletContext = getServletContext();
        if (servletContext == null) {
            throw new IllegalStateException("ServletContext property must be set via the setServletContext method.");
        }
        return servletContext;
    }

    protected void setContextAttribute(String key, Object value) {
        if (value == null) {
            removeContextAttribute(key);
        } else {
            getRequiredServletContext().setAttribute(key, value);
        }
    }

    protected Object getContextAttribute(String key) {
        return getRequiredServletContext().getAttribute(key);
    }

    protected void removeContextAttribute(String key) {
        getRequiredServletContext().removeAttribute(key);
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    protected StringBuilder toStringBuilder() {
        return new StringBuilder(super.toString());
    }
}

