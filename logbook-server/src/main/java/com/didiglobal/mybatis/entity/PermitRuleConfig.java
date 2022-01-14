package com.didiglobal.mybatis.entity;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
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
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
@Getter
@Setter
public class PermitRuleConfig implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JSONField(name="biz_event_type_id")
    private Long bizEventTypeId;

    @JSONField(name="permit_type")
    private Integer permitType;

    @JSONField(name="permit_context")
    private String permitContext;

    @JSONField(name="comment")
    private String comment;

    @JSONField(name="status")
    private Integer status;

}
