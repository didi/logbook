package com.didiglobal.bizconfig.manager.translate;

import com.didiglobal.mybatis.entity.TranslateRuleConfig;

import java.util.List;
import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/15 5:59 下午
 */
public interface TranslateRuleConfigManager {
    List<TranslateRuleConfig> getTranslateRuleConfig(Long bizEventType);

    void refreshBizEventTranslateRuleConfigMap(Map<Long,List<TranslateRuleConfig>> map);
}
