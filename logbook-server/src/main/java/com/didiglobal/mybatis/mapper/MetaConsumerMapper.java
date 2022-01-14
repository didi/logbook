package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.MetaConsumer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liyanling
 * @date 2021/11/17
 */
@Mapper
public interface MetaConsumerMapper {

    @Select("SELECT * FROM meta_consumer WHERE status = 1")
    List<MetaConsumer> queryValidMetaConsumer();

    // todo 需要一个 insert 接口
}
