package com.didiglobal.common.authz;

/**
 *
 * @author liyanling
 * @date 2021/10/29
 *  调用方信息：filter 写，resolver 读
 */
public class CallerInfo {
    String caller;

    public CallerInfo(String caller) {
        this.caller = caller;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }


    @Override
    public String toString() {
        return "CallerInfo{" +
                "caller='" + caller + '\'' +
                '}';
    }
}
