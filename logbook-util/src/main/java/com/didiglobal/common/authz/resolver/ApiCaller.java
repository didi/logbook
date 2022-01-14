package com.didiglobal.common.authz.resolver;

import java.lang.annotation.*;


/**
 * @author liyanling
 * @date 2021/10/29
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiCaller {

}
