package com.didiglobal.mediator.translate;


import com.didiglobal.common.pojo.LogbookConstants;
import com.didiglobal.common.pojo.vo.SingleTranslateVo;
import com.didiglobal.common.translate.TranslateInput;
import com.didiglobal.mybatis.entity.TranslateRuleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author liyanling
 * @date 2021/11/15 4:36 下午
 */
@Slf4j
@Component("groovyTranslateMediator")
public class GroovyTranslateMediator implements TranslateMediator{

    @Resource
    ScriptEngineManager scriptEngineManager;

    @Override
    public SingleTranslateVo<Object> translate(TranslateRuleConfig translateRuleConfig, TranslateInput translateInput) {
        String translateScript = translateRuleConfig.getTranslateContext();
        ScriptEngine engine = scriptEngineManager.getEngineByName(LogbookConstants.GROOVY);
        engine.put("translateInput", translateInput);
        try {
            Object obj = engine.eval(translateScript);
            log.debug("translate result："+obj);
            return SingleTranslateVo
                    .builder()
                    .success(true)
                    .result(obj)
                    .build();
        } catch (ScriptException e) {
            log.error("translate caught Exception,e=",e);
            return SingleTranslateVo
                    .builder()
                    .success(false)
                    .result("translate fail exception="+e.getMessage())
                    .build();
        }
    }
}
