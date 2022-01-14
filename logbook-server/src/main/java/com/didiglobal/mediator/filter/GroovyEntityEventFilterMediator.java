package com.didiglobal.mediator.filter;

import com.didiglobal.common.pojo.LogbookConstants;
import com.didiglobal.common.pojo.dto.EntityEventDto;
import com.didiglobal.common.pojo.vo.FilterVo;
import com.didiglobal.mybatis.entity.MappingConsumeRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author liyanling
 * @date 2021/11/22 3:56 下午
 */
@Slf4j
@Component
public class GroovyEntityEventFilterMediator implements EntityEventFilterMediator {

    @Resource
    ScriptEngineManager scriptEngineManager;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public FilterVo<Object> filter(EntityEventDto entityEventDto, MappingConsumeRule mappingConsumeRule) {
        String filterScript = mappingConsumeRule.getFilterScript();
        ScriptEngine engine = scriptEngineManager.getEngineByName(LogbookConstants.GROOVY);
        engine.put("filterInput", entityEventDto);
        try {
            Object obj = engine.eval(filterScript);
            return FilterVo.builder().success(true).result(objectMapper.writeValueAsString(obj)).build();
        } catch (ScriptException e) {
            log.error("filter caught exception,mappingConsumeRule={},e=", mappingConsumeRule, e);
            return FilterVo.builder().success(false).result(e.getMessage()).build();
        } catch (JsonProcessingException e) {
            log.error("filter caught exception,mappingConsumeRule={},e=", mappingConsumeRule, e);
            return FilterVo.builder().success(false).result(e.getMessage()).build();
        }
    }
}
