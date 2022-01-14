package com.didiglobal.mediator.permit;

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
public class PermitMediatorFactory {

    @Autowired
    PermitMediator jsonSchemaPermitMediator;

    /**
     * 工厂方法获取 permitMediator 实例
     *
     * 目前只有 jsonSchema 实现
     *
     * @param permitType
     * @return
     */
    public PermitMediator getPermitMediator(int permitType){
        if(permitType == PermitType.JSON_SCHEMA_PERMIT){
            return jsonSchemaPermitMediator;
        }else{
            log.error("unsupported permit_type:{}",permitType);
            throw new LogbookException(LogbookError.UNSUPPORTED_PERMIT_TYPE);
        }
    }

}
