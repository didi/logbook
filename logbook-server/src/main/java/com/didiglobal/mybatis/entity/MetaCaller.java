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
 * @author liyanling
 * @date 2021/11/16
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
@Getter
@Setter
public class MetaCaller implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JSONField(name="caller_name")
    private String callerName;

    @JSONField(name="caller_credential")
    private String callerCredential;

    @JSONField(name="comment")
    private String comment;

    @JSONField(name="status")
    private int status;
}
