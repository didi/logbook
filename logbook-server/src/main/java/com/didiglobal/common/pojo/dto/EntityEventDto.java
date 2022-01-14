package com.didiglobal.common.pojo.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mayingdong
 * @date 2021/11/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityEventDto {

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty("span_id")
    private String spanId;

    @JsonProperty("entity_type")
    private Long entityType;

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("entity_info_before_event")
    private JSONObject entityInfoBeforeEvent;

    @JsonProperty("entity_info_after_event")
    private JSONObject entityInfoAfterEvent;

    @JsonProperty("event_type")
    private Long eventType;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_time")
    private long eventTime;

    @JsonProperty("event_report_system")
    private String eventReportSystem;

    @JsonProperty("event_info")
    private JSONObject eventInfo;

}
