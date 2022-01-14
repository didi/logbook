package com.didiglobal.config.mqplugins;

import com.didiglobal.common.pojo.vo.ConsumeVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyanling
 * @date 2021/11/23 5:28 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageQueueTraceContext {
    @JsonProperty("tag")
    private String tag;// com_request_in/com_request_oud

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("span_id")
    private String spanId;

    @JsonProperty("thread_name")
    private String threadName;

    @JsonProperty("consumer_group_name")
    private String consumerGroupName;// 消费组

    @JsonProperty("consumer_topic_name")
    private String consumerTopicName;// 消费到的 topic

    @JsonProperty("timestamp")
    private long timestamp;// 日志打印时间

    @JsonProperty("cost")
    private int cost;// 处理时长

    @JsonProperty("consume_message")
    private String consumeMessage;// 消费到的原文

    @JsonProperty("success")
    private boolean success;// 处理结果

    @JsonProperty("failure_exception")
    private Throwable failureException;// 失败信息

    @JsonProperty("consume_vo")
    private ConsumeVo<Object> consumeVo;// 消费结果

}
