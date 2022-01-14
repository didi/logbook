package com.didiglobal.bizconfig.manager.entitytype;

import com.didiglobal.mybatis.entity.MetaEntityType;

import java.util.Map;

/**
 * @author liyanling
 * @date 2021/11/17 4:13 下午
 */
public interface EntityTypeManager {

    // 判断 entityTypeId 是否存在
    boolean isExist(Long entityTypeId);

    void refreshEntityTypeMap(Map<Long, MetaEntityType> map);
}
