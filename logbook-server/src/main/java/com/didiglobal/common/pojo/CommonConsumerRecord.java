package com.didiglobal.common.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyanling
 * @date 2022/1/6 4:47 下午
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonConsumerRecord {
    // topic名称
    @JsonProperty("topic_name")
    private String topicName;

    // 消费到的消息原文
    @JsonProperty("value")
    private String value;

}
