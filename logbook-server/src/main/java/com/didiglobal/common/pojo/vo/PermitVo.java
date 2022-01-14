package com.didiglobal.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@Data
@Builder
public class PermitVo<T> {
    // 准入是否成功
    @JsonProperty("success")
    private boolean success = false;
    @JsonProperty("result")
    private T result;
}
