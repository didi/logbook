package com.didiglobal.common.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
public class JsonSchemaUtil <T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaUtil.class);

    /**
     * 给定 json 字符串 jsonReq，用 jsonSchema 做格式校验
     *
     * @param jsonReq
     * @param jsonSchema
     * @return
     */
    public static JsonSchemaValidateResult validate(String jsonReq, String jsonSchema) throws Exception {
        try {
            ProcessingReport report = JsonSchemaFactory
                    .byDefault()
                    .getJsonSchema(JsonLoader.fromString(jsonSchema))
                    .validate(JsonLoader.fromString(jsonReq));


            // 获取完整解析报告。
            // isSuccess 为 true 表示符合格式，false 表示不符合
            List<JsonNode> messages = getMessageList(report);
            return JsonSchemaValidateResult
                    .builder()
                    .logLevel(report.getLogLevel())
                    .exceptionThreshold(report.getExceptionThreshold())
                    .isSuccess(report.isSuccess())
                    .messages(messages)
                    .build();

        } catch (Exception e) {
            LOGGER.error("validator caught exception,json={},jsonSchema={},exception=",
                    jsonReq.toString(), jsonSchema.toString(), e);
            throw e;
        }
    }

    private static List<JsonNode> getMessageList(ProcessingReport report) {
        List<JsonNode> list = new ArrayList<>();
        for (ProcessingMessage processingMessage : report) {
            list.add(processingMessage.asJson());
        }
        return list;
    }

}
