package com.didiglobal.common.web.filter;

import javax.servlet.FilterConfig;

public abstract class NameableFilter extends AbstractFilter implements Nameable {

    /**
     * name在一个web应用内应该唯一
     */
    private String name;

    protected String getName() {
        if (this.name == null) {
            FilterConfig config = getFilterConfig();
            if (config != null) {
                this.name = config.getFilterName();
            }
        }
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    protected StringBuilder toStringBuilder() {
        String name = getName();
        if (name == null) {
            return super.toStringBuilder();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            return sb;
        }
    }
}
