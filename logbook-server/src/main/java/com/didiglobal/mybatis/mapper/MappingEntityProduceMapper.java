package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.MappingEntityProducer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liyanling
 * @date 2021/11/17
 */
@Mapper
public interface MappingEntityProduceMapper {

    @Select("SELECT * FROM mapping_entity_produce WHERE status = 1")
    List<MappingEntityProducer> queryAllMappingItem();

}
