package com.didiglobal.bizconfig.manager.consumerule;

import com.didiglobal.mybatis.entity.MappingConsumeRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/18 4:18 下午
 */
@Component
@Slf4j
public class MappingConsumeRuleManagerImpl implements MappingConsumeRuleManager {
    private Map<String,MappingConsumeRule> mappingConsumeRuleMap;

    public MappingConsumeRuleManagerImpl() {
        this.mappingConsumeRuleMap = new ConcurrentHashMap<>();
    }

    @Override
    public void refreshConsumeRuleMap(Map<String, MappingConsumeRule> map) {
        this.mappingConsumeRuleMap = map;
        log.info("MappingConsumeRuleManagerImpl refresh success,size={}",this.mappingConsumeRuleMap.size());
    }

    @Override
    public MappingConsumeRule getConsumeRule(String consumerGroupName) {
        return mappingConsumeRuleMap.get(consumerGroupName);
    }
}
