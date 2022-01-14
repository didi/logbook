package com.didiglobal.common.authz;

/**
 * @author liyanling
 * @date 2021/10/29
 * 认证通过后的 attributes 信息：filter 写，resolver 读
 */
public class CallerAuthConstant {

    // 把 callerInfo 写到 request，服务层可以从这个 key 里取 CallerInfo 信息
    public static final String API_CALLER_INFO = "didiglobal.api.caller_info";

}
