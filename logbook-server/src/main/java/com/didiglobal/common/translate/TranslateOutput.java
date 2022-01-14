package com.didiglobal.common.translate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyanling
 * @date 2021/11/17 11:28 上午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslateOutput {

    @JsonProperty("event_type")
    private Long eventType;

    @JsonProperty("event_time")
    private long eventTime;

    @JsonProperty("event_info")
    private JSONObject eventInfo;


//    // todo 脚本里不写，用 eventType 反查
//    @JsonProperty("entity_type")
//    private Long entityType;

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty("entity_info_before_event")
    private JSONObject entityInfoBeforeEvent;

    @JsonProperty("entity_info_after_event")
    private JSONObject entityInfoAfterEvent;

}
