package com.didiglobal.bizconfig.manager.caller;

import com.didiglobal.common.authz.filter.CallerConfig;
import com.didiglobal.common.authz.filter.CallerConfigProvider;
import com.didiglobal.mybatis.entity.MetaCaller;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/16 3:43 下午
 *
 * 注意：这是所有 manager 里最特殊的一个。
 * 它 实现了 logbook-util 中的 CallerConfigProvider 接口，用于对请求方进行鉴权
 */
public interface CallerConfigManager extends CallerConfigProvider {

    CallerConfig getCallerConfig(String caller);

    void refreshCallerConfigMap(Map<String, MetaCaller> callerMap);
}
