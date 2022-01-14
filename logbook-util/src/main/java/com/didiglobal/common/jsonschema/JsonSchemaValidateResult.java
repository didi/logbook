package com.didiglobal.common.jsonschema;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.report.LogLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@Data
@Builder
public class JsonSchemaValidateResult {

    @JSONField(name = "log_level")
    private LogLevel logLevel;

    @JsonProperty("exception_threshold")
    private LogLevel exceptionThreshold;

    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("validate_result")
    private List<JsonNode> messages;
}

