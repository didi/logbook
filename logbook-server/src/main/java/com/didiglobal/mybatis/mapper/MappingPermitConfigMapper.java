package com.didiglobal.mybatis.mapper;

import com.didiglobal.mybatis.entity.PermitRuleConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@Mapper
public interface MappingPermitConfigMapper {

//    @Select("SELECT * FROM biz_event_permit_config WHERE biz_event_type_id=#{bizEventTypeId}")
//    PermitRuleConfig queryByBizEventTypeId(Long bizEventTypeId);

    @Select("SELECT * FROM mapping_permit_config WHERE status = 1")
    List<PermitRuleConfig> queryAllValidPermitConfig();

}
