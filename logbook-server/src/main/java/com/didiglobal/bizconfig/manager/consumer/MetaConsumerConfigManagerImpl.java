package com.didiglobal.bizconfig.manager.consumer;

import com.didiglobal.api.biz.service.ConsumerProcessor;
import com.didiglobal.common.pojo.ConsumerInfo;
import com.didiglobal.mybatis.entity.MetaConsumer;
import com.didiglobal.mq.consumer.ConsumeGenerator;
import com.didiglobal.mq.consumer.IConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liyanling
 * @date 2021/11/18 4:34 下午
 */
@Component
@Slf4j
public class MetaConsumerConfigManagerImpl implements MetaConsumerConfigManager {

    private final Map<String, ConsumerInfo> consumerInfoMap;

    @Autowired
    ConsumeGenerator<Object> consumeGenerator;

    @Autowired
    ConsumerProcessor<Object> consumerProcessor;

    public MetaConsumerConfigManagerImpl() {
        this.consumerInfoMap = new ConcurrentHashMap<>();
    }

    @Override
    public void refreshConsumerConfig(boolean startConsumeRightNow, Map<String, MetaConsumer> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        refresh(startConsumeRightNow, map);
    }

    private void refresh(boolean startConsumeRightNow, Map<String, MetaConsumer> map) {
        if (consumerInfoMap.isEmpty()) {
            // 直接遍历新 map，生成 consumer 后插入到 this.map 里
            for (String consumerGroupName : map.keySet()) {
                MetaConsumer metaConsumer = map.get(consumerGroupName);
                IConsumer consumer = createAndStartConsumer(startConsumeRightNow, metaConsumer);
                this.consumerInfoMap.put(consumerGroupName, new ConsumerInfo(metaConsumer, consumer));
            }
        } else {
            // 遍历新 map：没有则插入，已有则更新
            for (String consumerGroupName : map.keySet()) {
                MetaConsumer metaConsumer = map.get(consumerGroupName);
                if (this.consumerInfoMap.containsKey(consumerGroupName)) {
                    // 已有、配置未更新，continue
                    if (this.consumerInfoMap.get(consumerGroupName).getMetaConsumer().equals(metaConsumer)) {
                        continue;
                    }
                    // 已有、配置有更新，启动新的 consumer「旧的 consumer 要 close」
                    IConsumer oldConsumer = this.consumerInfoMap.get(consumerGroupName).getConsumer();
                    IConsumer newConsumer = createAndStartConsumer(startConsumeRightNow, metaConsumer);
                    this.consumerInfoMap.put(consumerGroupName, new ConsumerInfo(metaConsumer, newConsumer));
                    oldConsumer.close();
                } else {
                    // 没有，插入
                    IConsumer consumer = createAndStartConsumer(startConsumeRightNow, metaConsumer);
                    this.consumerInfoMap.put(consumerGroupName, new ConsumerInfo(metaConsumer, consumer));
                }
            }
            // 遍历老 map：如果在新 map 里没出现，则 移除
            Iterator<Map.Entry<String, ConsumerInfo>> it = this.consumerInfoMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, ConsumerInfo> entry = it.next();
                if (!map.containsKey(entry.getKey())) {
                    entry.getValue().getConsumer().close();
                    it.remove();
                }
            }
        }
        log.info("MetaConsumerConfigManagerImpl refresh success,size={}", this.consumerInfoMap.size());
    }

    private IConsumer createAndStartConsumer(boolean startConsumeRightNow, MetaConsumer metaConsumer) {
        IConsumer consumer = consumeGenerator.generate(metaConsumer, consumerProcessor);
        if (startConsumeRightNow) {
            consumer.startConsume();
        }
        return consumer;
    }


}
