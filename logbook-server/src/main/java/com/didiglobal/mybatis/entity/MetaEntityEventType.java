package com.didiglobal.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author liyanling
 * @date 2021/11/16
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MetaEntityEventType implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long entityTypeId;

    private String name;

    private String comment;

    private int status;
}
