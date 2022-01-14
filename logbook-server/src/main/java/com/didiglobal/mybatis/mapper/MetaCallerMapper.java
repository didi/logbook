package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.MetaCaller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@Mapper
public interface MetaCallerMapper {

    @Select("SELECT * FROM meta_caller WHERE status = 1")
    List<MetaCaller> queryAllValidCaller();

    // todo 需要一个 insert 接口
}
