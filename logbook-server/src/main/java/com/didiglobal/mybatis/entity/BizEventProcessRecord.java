package com.didiglobal.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mayingdong
 * @date 2020/11/30
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@JSONType(naming = PropertyNamingStrategy.SnakeCase)
//@Getter
//@Setter
public class BizEventProcessRecord implements Serializable {
//    private static final long serialVersionUID = 0L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上报消息唯一id：由 caller.biz_event_id 组成
     */
    private String globalBizEventId;

    /**
     * 上报消息处理状态。0-未处理，1-处理完成
     */
    private Integer status;

    /**
     * 数据上报系统
     */
    private String callerName;

    /**
     * 调用接口时产生的trace_id
     */
    private String traceId;

    /**
     * 调用接口时产生的span_id
     */
    private String spanId;

    /**
     * 上报数据的具体内容
     */
    private String bizEventBody;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 翻译同步完成时间
     */
    private Date successTime;
    /**
     * 作废时间
     */
    private Date abortTime;
}
