package com.didiglobal.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@Data
@Builder
public class ReportVo<T> {
    // 上报是否成功
    @JsonProperty("success")
    private boolean success = false;

    @JsonProperty("result")
    private T result;

    @JsonProperty("translate_and_sync_results")
    private List<TranslateAndSyncResult> translateAndSyncResults;
}
