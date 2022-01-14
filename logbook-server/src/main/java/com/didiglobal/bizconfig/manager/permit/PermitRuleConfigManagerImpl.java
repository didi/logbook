package com.didiglobal.bizconfig.manager.permit;

import com.didiglobal.mybatis.entity.PermitRuleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/15 4:57 下午
 */
@Component
@Slf4j
public class PermitRuleConfigManagerImpl implements PermitRuleConfigManager {
    private Map<Long, PermitRuleConfig> permitRuleConfigMap;

    public PermitRuleConfigManagerImpl() {
        permitRuleConfigMap = new ConcurrentHashMap<>();
    }

    @Override
    public void refreshPermitRuleConfigMap(Map<Long,PermitRuleConfig> map){
        this.permitRuleConfigMap = map;
        log.info("PermitRuleConfigManagerImpl refresh success,size={}", this.permitRuleConfigMap.size());
    }

    @Override
    public PermitRuleConfig getPermitRuleConfig(Long bizEventType) {
        return permitRuleConfigMap.get(bizEventType);
    }
}
