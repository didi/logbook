package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.MetaEntityType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liyanling
 * @date 2021/11/17
 */
@Mapper
public interface MetaEntityTypeMapper {

    @Select("SELECT * FROM meta_entity_type WHERE status = 1")
    List<MetaEntityType> queryAllValidEntityType();

    // todo 需要一个 insert 接口
}
