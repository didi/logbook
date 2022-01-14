package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.MetaEntityEventType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liyanling
 * @date 2021/11/17
 */
@Mapper
public interface MetaEntityEventTypeMapper {

    @Select("SELECT * FROM meta_entity_event_type WHERE status = 1")
    List<MetaEntityEventType> queryAllValidEntityEventType();

    // todo 需要一个 insert 接口
}
