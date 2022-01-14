package com.didiglobal.mediator.filter;

import com.didiglobal.common.LogbookError;
import com.didiglobal.common.LogbookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liyanling
 * @date 2021/11/22 3:57 下午
 */
@Slf4j
@Component
public class EntityEventFilterFactory {

    @Autowired
    EntityEventFilterMediator noneEntityEventFilterMediator;

    @Autowired
    EntityEventFilterMediator groovyEntityEventFilterMediator;


    public EntityEventFilterMediator getEntityEventFilterMediator(int filterType){
        if(filterType == EntityEventFilterType.FILTER_NONE){
            return noneEntityEventFilterMediator;
        }else if(filterType == EntityEventFilterType.FILTER_GROOVY){
            return groovyEntityEventFilterMediator;
        }else{
            log.error("unsupported filter_type:{}",filterType);
            throw new LogbookException(LogbookError.UNSUPPORTED_FILTER_TYPE);
        }
    }
}
