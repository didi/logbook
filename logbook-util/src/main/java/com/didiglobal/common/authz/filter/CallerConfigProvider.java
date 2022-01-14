package com.didiglobal.common.authz.filter;

/**
 * @author liyanling
 * @date 2021/10/29
 * caller 配置数据Provider
 */
public interface CallerConfigProvider {
    /**
     * 入参：caller
     * 返回值：caller + credencial
     * @param caller
     * @return
     */
    CallerConfig getCallerConfig(String caller);
}
