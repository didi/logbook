package com.didiglobal.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@JSONType(naming = PropertyNamingStrategy.SnakeCase)
//@Getter
//@Setter
public class MappingConsumeRule implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String consumerGroupName;

    private Integer filterType;

    private String filterScript;

    private String dispatchDestination;

    private String comment;

    private Integer status;

}
