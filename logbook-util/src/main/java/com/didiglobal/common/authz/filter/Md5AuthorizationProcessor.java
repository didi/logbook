package com.didiglobal.common.authz.filter;

import com.didiglobal.common.authz.CallerInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * @author liyanling
 * @date 2021/10/29
 * md5 算法
 */
public class Md5AuthorizationProcessor implements AuthorizationProcessor {

    // 生成 authorization 的模板 ${caller}.${credential}
    private static final String SIGN_TMPL = "%s.%s";

    /**
     * 生成 authorization
     *
     * @param caller
     * @param callerConfigProvider
     * @return
     */
    @Override
    public String generateAuthorization(String caller, CallerConfigProvider callerConfigProvider)
            throws IllegalAccessException {
        CallerConfig callerConfig = callerConfigProvider.getCallerConfig(caller);
        if (null == callerConfig) {
            throw new IllegalAccessException("no config for caller:"+caller);
        }
        return generateAuthorization(callerConfig.getCaller(),callerConfig.getCredential());
    }



    /**
     * 用来对比 Authorization 对不对
     *
     * 返回null表示不匹配
     * 抛异常表示没配置
     *
     * @param caller
     * @param callerConfigProvider
     * @return
     */
    @Override
    public CallerInfo validate(String caller, CallerConfigProvider callerConfigProvider, String targetAuthorization)
            throws IllegalAccessException {
        String calculatedAuthorization = generateAuthorization(caller,callerConfigProvider);
        boolean isMatch =  StringUtils.equalsIgnoreCase(calculatedAuthorization, targetAuthorization);
        if(isMatch){
            return new CallerInfo(caller);
        }else{
            return null;
        }
    }

    private String generateAuthorization(String caller,String credential){
        return DigestUtils.md5DigestAsHex(String.format(SIGN_TMPL,caller,credential).getBytes());
    }
}
