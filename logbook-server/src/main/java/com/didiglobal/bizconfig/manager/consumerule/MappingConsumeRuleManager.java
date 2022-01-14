package com.didiglobal.bizconfig.manager.consumerule;

import com.didiglobal.mybatis.entity.MappingConsumeRule;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/18 4:09 下午
 */
public interface MappingConsumeRuleManager {

    void refreshConsumeRuleMap(Map<String, MappingConsumeRule> map);

    MappingConsumeRule getConsumeRule(String consumerGroupName);

}
