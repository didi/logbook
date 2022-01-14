package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.MetaProducer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liyanling
 * @date 2021/11/17
 */
@Mapper
public interface MetaProducerMapper {

    @Select("SELECT * FROM meta_producer WHERE status = 1")
    List<MetaProducer> queryAllValidProducerConfig();

    // todo 需要一个 insert 接口
}
