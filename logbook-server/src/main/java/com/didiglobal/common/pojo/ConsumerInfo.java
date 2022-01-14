package com.didiglobal.common.pojo;

import com.didiglobal.mybatis.entity.MetaConsumer;
import com.didiglobal.mq.consumer.IConsumer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author liyanling
 * @date 2021/11/18 5:20 下午
 */
@Data
@Builder
@AllArgsConstructor
public class ConsumerInfo {
    @JsonProperty("meta_consumer")
    private MetaConsumer metaConsumer;

    @JsonProperty("consumer")
    private IConsumer consumer;
}
