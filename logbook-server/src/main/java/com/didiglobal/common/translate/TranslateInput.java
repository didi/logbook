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
public class TranslateInput {

    // ---------------------
    //  上游上报的原始信息，翻译过程中一定会用到
    // ---------------------

    // 业务事件类型
    @JsonProperty("biz_event_type")
    private Long bizEventType;

    // 业务事件id
    @JsonProperty("biz_event_id")
    private String bizEventId;

    // 业务事件信息，时间、地点、上下文，以及除了主体外的其他实体的信息
    @JsonProperty("biz_event_info")
    private JSONObject bizEventInfo;

    // 参与业务事件的主体信息（1-n个实体）
    @JsonProperty("biz_entity_info")
    private JSONObject bizEntityInfo;

    // 对于变更类的事件，可能需要同时上报变更前的业务事件的主体信息
    @JsonProperty("biz_entity_info_before")
    private JSONObject bizEntityInfoBefore;


    // ---------------------
    //  logbook 加工后的信息，翻译过程中可用可不用
    // ---------------------


    // 调用方名称
    @JsonProperty( "caller_name")
    private String callerName;


    // 全局业务事件id
    @JsonProperty("global_biz_event_id")
    private String globalBizEventId;

    // traceid
    @JsonProperty("trace_id")
    private String traceId;

    // spanid
    @JsonProperty("span_id")
    private String spanId;

}
