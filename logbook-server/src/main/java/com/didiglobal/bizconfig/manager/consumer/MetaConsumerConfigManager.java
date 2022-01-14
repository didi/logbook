package com.didiglobal.bizconfig.manager.consumer;

import com.didiglobal.mybatis.entity.MetaConsumer;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/18 4:08 下午
 */
public interface MetaConsumerConfigManager {

    // 刷新 consumer 配置
    void refreshConsumerConfig(boolean startConsumeRightNow, Map<String, MetaConsumer> map);

}
