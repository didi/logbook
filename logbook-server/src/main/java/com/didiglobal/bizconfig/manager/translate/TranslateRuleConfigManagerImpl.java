package com.didiglobal.bizconfig.manager.translate;

import com.didiglobal.mybatis.entity.TranslateRuleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/15 5:59 下午
 */
@Component
@Slf4j
public class TranslateRuleConfigManagerImpl implements TranslateRuleConfigManager{
    private Map<Long,List<TranslateRuleConfig>> bizEventTranslateRuleConfigMap;

    public TranslateRuleConfigManagerImpl() {
        this.bizEventTranslateRuleConfigMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<TranslateRuleConfig> getTranslateRuleConfig(Long bizEventType) {
        return bizEventTranslateRuleConfigMap.get(bizEventType);
    }

    @Override
    public void refreshBizEventTranslateRuleConfigMap(Map<Long, List<TranslateRuleConfig>> map) {
        this.bizEventTranslateRuleConfigMap = map;
        log.info("TranslateRuleConfigManagerImpl refresh success,size={}", this.bizEventTranslateRuleConfigMap.size());
    }
}
