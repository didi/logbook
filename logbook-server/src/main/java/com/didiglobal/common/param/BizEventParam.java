package com.didiglobal.common.param;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author mayingdong
 * @date 2021/11/3
 *
 */
@Data
public class BizEventParam {
    /**
     * 业务事件类型
     */
    @NotNull(message = "业务事件类型不能为空")
    @JsonProperty("biz_event_type")
    private Long bizEventType;

    /**
     * 业务事件id，是业务事件的唯一标识
     */
    @NotEmpty(message = "业务事件唯一键不能为空")
    @JsonProperty("biz_event_id")
    private String bizEventId;

    /**
     * 业务事件信息，时间、地点、上下文，以及除了主体外的其他实体的信息
     */
    @NotNull(message = "业务事件信息不能为空")
    @JsonProperty("biz_event_info")
    private JSONObject bizEventInfo;

    /**
     * 参与业务事件的主体信息（1-n个实体）
     */
    @NotNull(message = "业务事件的实体信息不能为空")
    @JsonProperty("biz_entity_info")
    private JSONObject bizEntityInfo;

    /**
     * 对于变更类的事件，可能需要同时上报变更前的业务事件的主体信息
     */
    @JsonProperty("biz_entity_info_before")
    private JSONObject bizEntityInfoBefore;

}
