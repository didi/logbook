package com.didiglobal.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author liyanling
 * @date 2021/11/15
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@JSONType(naming = PropertyNamingStrategy.SnakeCase)
@Getter
@Setter
public class MetaConsumer implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonProperty("consumer_group_name")
    private String consumerGroupName;

    @JsonProperty("consumer_context")
    private String consumerContext;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("status")
    private Integer status;

}
