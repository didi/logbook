package com.didiglobal.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
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
public class MappingEntityProducer implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long entityTypeId;
    private String topicName;
    private String comment;
    private Integer status;

}
