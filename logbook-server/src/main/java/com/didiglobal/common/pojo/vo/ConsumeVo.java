package com.didiglobal.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author liyanling
 * @date 2021/11/18
 */
@Data
@Builder
public class ConsumeVo<T> {
    @JsonProperty("success")
    private boolean success = false;

    @JsonProperty("need_retry")
    private boolean needRetry = false;

    @JsonProperty("result")
    private T result;
}
