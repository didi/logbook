package com.didiglobal.mediator.filter;

import com.didiglobal.common.pojo.dto.EntityEventDto;
import com.didiglobal.common.pojo.vo.FilterVo;
import com.didiglobal.mybatis.entity.MappingConsumeRule;

/**
 * @author liyanling
 * @date 2021/11/15 4:13 下午
 */
public interface EntityEventFilterMediator {

    /**
     * 使用 permitRuleConfig 中的 permit_type 对应的准入方案实现
     * 对业务事件 bizEventParam 进行准入校验，最终返回带详情的校验结果
     */
    FilterVo<Object> filter(EntityEventDto entityEventDto, MappingConsumeRule mappingConsumeRule);

}
