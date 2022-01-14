package com.didiglobal.mediator.translate;

import com.didiglobal.common.LogbookError;
import com.didiglobal.common.LogbookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liyanling
 * @date 2021/11/15 4:11 下午
 */
@Slf4j
@Component
public class TranslateMediatorFactory {

    @Autowired
    TranslateMediator groovyTranslateMediator;

    /**
     * 工厂方法获取 translateMediator 实例
     *
     * 目前只有 groovy 实现
     *
     * @param translateType
     * @return
     */
    public TranslateMediator getTranslateMediator(int translateType){
        if(translateType == TranslateType.GROOVY_TRANSLATE){
            return groovyTranslateMediator;
        }else{
            log.error("unsupported translate_type:{}",translateType);
            throw new LogbookException(LogbookError.UNSUPPORTED_TRANSLATE_TYPE);
        }
    }

}
