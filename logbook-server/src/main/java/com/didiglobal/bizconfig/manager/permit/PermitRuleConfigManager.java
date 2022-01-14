package com.didiglobal.bizconfig.manager.permit;

import com.didiglobal.mybatis.entity.PermitRuleConfig;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/15 4:56 下午
 * <p>
 * 业务事件对应的准入配置管理
 */
public interface PermitRuleConfigManager {

    /**
     * 根据 业务事件类型 获取对应的准入配置
     *
     * @param bizEventType
     * @return
     */
    PermitRuleConfig getPermitRuleConfig(Long bizEventType);


    void refreshPermitRuleConfigMap(Map<Long, PermitRuleConfig> map);

//    PermitRuleConfig addToPermitRuleConfigMap(String bizEventType,PermitRuleConfig permitRuleConfig);
//
//    PermitRuleConfig removeFromPermitRuleConfigMap(String bizEventType);

}
