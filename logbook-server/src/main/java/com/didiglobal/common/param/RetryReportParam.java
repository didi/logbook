package com.didiglobal.common.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liyanling
 * @date 2021/11/23 8:16 下午
 */
@Data
public class RetryReportParam {
    @NotNull(message = "业务事件id不能为空")
    @JsonProperty("global_biz_event_id")
    private String globalBizEventId;
}
