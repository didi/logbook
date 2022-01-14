package com.didiglobal.common.authz.filter;

import com.didiglobal.common.authz.CallerInfo;

/**
 * @author liyanling
 * @date 2021/10/29
 */
public interface AuthorizationProcessor {

    /**
     * 用 callerConfig 的 caller+credencial，生成 authorization 的算法
     *
     * @param caller
     * @param callerConfigProvider
     * @return
     */
    String generateAuthorization(String caller, CallerConfigProvider callerConfigProvider)
            throws IllegalAccessException;

    /**
     *
     * 已知 caller，通过 callerConfigProvider 读到配置，然后校验
     * 用 callerConfig 的 caller+credencial，校验 targetAuthorization 对不对
     * 如果校验成功，则返回 CallerInfo
     *
     * @return
     */
    CallerInfo validate(String caller , CallerConfigProvider callerConfigProvider, String targetAuthorization)
            throws IllegalAccessException;

}
