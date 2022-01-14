package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.MappingConsumeRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liyanling
 * @date 2021/11/18
 */
@Mapper
public interface MappingConsumeRuleMapper {

    @Select("SELECT * FROM mapping_consume_rule WHERE status = 1")
    List<MappingConsumeRule> queryValidConsumeRule();

}
