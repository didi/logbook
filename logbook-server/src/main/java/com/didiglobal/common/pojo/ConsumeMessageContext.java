package com.didiglobal.common.pojo;

import com.didiglobal.common.pojo.dto.EntityEventDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyanling
 * @date 2021/11/19 3:24 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumeMessageContext {
    @JsonProperty("topic_name")
    private String topicName;

    @JsonProperty("thread_name")
    private String threadName;

    @JsonProperty("consume_trace_id")
    private String consumeTraceId;

    @JsonProperty("consume_span_id")
    private String consumeSpanId;

    @JsonProperty("consume_global_event_id")
    private String consumeGlobalEventId;

    @JsonProperty("consume_group_name")
    private String consumeGroupName;

    @JsonProperty("consume_message_body")
    private String consumeMessageBody;

    @JsonProperty("entity_event_dto")
    private EntityEventDto entityEventDto;

    // ......

}
