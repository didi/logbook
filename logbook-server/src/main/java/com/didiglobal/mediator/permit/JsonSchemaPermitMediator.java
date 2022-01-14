package com.didiglobal.mediator.permit;

import com.didiglobal.common.LogbookError;
import com.didiglobal.common.LogbookException;
import com.didiglobal.common.jsonschema.JsonSchemaUtil;
import com.didiglobal.common.jsonschema.JsonSchemaValidateResult;
import com.didiglobal.common.param.BizEventParam;
import com.didiglobal.common.pojo.vo.PermitVo;
import com.didiglobal.mybatis.entity.PermitRuleConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liyanling
 * @date 2021/11/15 4:14 下午
 */
@Component("jsonSchemaPermitMediator")
@Slf4j
public class JsonSchemaPermitMediator implements PermitMediator {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 业务事件 jsonSchema  准入实现
     *
     * @param bizEventParam
     * @param permitRuleConfig
     * @return
     */
    @Override
    public PermitVo<Object> permit(BizEventParam bizEventParam, PermitRuleConfig permitRuleConfig) {
        // 1. 准入配置检查（前面已经做过 null 检查，理论上这里不会出现 null==permitRuleConfig，纯兜底）
        if (null == permitRuleConfig) {
            log.warn("permit failed,biz_event_type no permit config");
            return PermitVo
                    .builder()
                    .success(false)
                    .result("biz_event_type no permit config")
                    .build();
        }

        // 2. 入参序列化为 json 字符串
        String jsonSchema = permitRuleConfig.getPermitContext();
        String reqJson = parseToString(bizEventParam);

        // 3. 用 jsonSchema 检测 json 字符串是否符合要求
        try {
            JsonSchemaValidateResult result = JsonSchemaUtil.validate(reqJson, jsonSchema);
            return PermitVo.builder().success(result != null && result.isSuccess()).result(result).build();
        } catch (Exception e) {
            log.warn("jsonSchema validate caught exception,jsonSchema={},e=", jsonSchema, e);
            throw new LogbookException(LogbookError.FAILED_PERMIT, "jsonSchema validate exception:" + e.getMessage());
        }

    }

    private String parseToString(BizEventParam bizEventParam) {
        try {
            return objectMapper.writeValueAsString(bizEventParam);
        } catch (JsonProcessingException e) {
            log.error("parseToString failed,bizEventParam={}",bizEventParam);
            throw new LogbookException(LogbookError.FAILED_SERIALIZE_TO_STRING);
        }
    }

}
