package com.didiglobal.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author liyanling
 * @date 2021/11/15
 */
@Data
@Builder
public class AbortVo<T> {

    @JsonProperty("success")
    private boolean success = false;

    @JsonProperty("result")
    private T result;
}
