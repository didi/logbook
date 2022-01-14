package com.didiglobal.mediator.filter;

import com.didiglobal.common.pojo.dto.EntityEventDto;
import com.didiglobal.common.pojo.vo.FilterVo;
import com.didiglobal.mybatis.entity.MappingConsumeRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liyanling
 * @date 2021/11/22 3:56 下午
 */
@Slf4j
@Component
public class NoneEntityEventFilterMediator implements EntityEventFilterMediator {

    @Override
    public FilterVo<Object> filter(EntityEventDto entityEventDto, MappingConsumeRule mappingConsumeRule) {
        return FilterVo.builder().success(true).result(entityEventDto).build();
    }
}
