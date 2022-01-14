package com.didiglobal.common.pojo.vo;

import com.didiglobal.common.pojo.dto.EntityEventDto;
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
public class TranslateAndSyncResult {
    /**
     * 最终的 翻译+生产 结果
     */
    @JsonProperty("final_success")
    private boolean finalSuccess;


    // 从这往下是翻译结果

    /**
     * 是否翻译成功
     */
    @JsonProperty("translate_success")
    private boolean translateSuccess = false;

    /**
     * 翻译结果
     */
    @JsonProperty("translate_success_result")
    private EntityEventDto translateSuccessResult;

    /**
     * 翻译过程中遇到的异常
     */
    @JsonProperty("translate_fail_reason")
    private String translateFailReason;


    // 从这往下是生产结果
    /**
     * 是否生产成功
     */
    @JsonProperty("produce_success")
    private boolean produceSuccess = false;

    /**
     * 生产成功结果
     */
    @JsonProperty("produce_success_result")
    private ProduceVo<Object> produceSuccessResult;

    /**
     * 生产失败原因
     */
    @JsonProperty("produce_fail_reason")
    private String produceFailReason;

}
