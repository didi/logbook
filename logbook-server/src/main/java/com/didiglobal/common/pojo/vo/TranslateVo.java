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
public class TranslateVo {
    // 整体翻译是否成功
    @JsonProperty("all_success")
    private boolean allSuccess = false;

    @JsonProperty("translate_and_sync_results")
    private List<TranslateAndSyncResult> translateAndSyncResults;
}
