package com.didiglobal.common.authz.filter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liyanling
 * @date 2021/10/29
 * caller + credencial 对
 */
public class CallerConfig {

    private String caller;
    private String credential;


    public CallerConfig(String caller, String credential) {
        this.caller = caller;
        this.credential = credential;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "CallerConfig{" + "caller='" + caller + '\'' + ", credential='" + credential + '\'' + '}';
    }


    public static Builder custom() {
        return new Builder();
    }

    public static class Builder {

        String caller;
        transient String credential;

        public Builder caller(String caller) {
            this.caller = caller;
            return this;
        }

        public Builder credential(String credential) {
            this.credential = credential;
            return this;
        }

        public CallerConfig build() {
            if (StringUtils.isAnyBlank(caller, credential)) {
                throw new IllegalArgumentException("caller & credential cannot be blank");
            }
            return new CallerConfig(caller, credential);
        }
    }
}
