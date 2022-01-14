package com.didiglobal.bizconfig.manager.caller;

import com.didiglobal.common.authz.filter.CallerConfig;
import com.didiglobal.mybatis.entity.MetaCaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/16 3:45 下午
 */
@Component
@Slf4j
public class CallerConfigManagerImpl implements CallerConfigManager{

    private Map<String, MetaCaller> callerMap;

    public CallerConfigManagerImpl() {
        callerMap = new ConcurrentHashMap<>();
    }

    @Override
    public CallerConfig getCallerConfig(String caller) {
        return new CallerConfig(caller,callerMap.get(caller).getCallerCredential());
    }

    @Override
    public void refreshCallerConfigMap(Map<String, MetaCaller> callerMap) {
        this.callerMap = callerMap;
        log.info("CallerConfigManagerImpl refresh success,size={}", this.callerMap.size());
    }

}
