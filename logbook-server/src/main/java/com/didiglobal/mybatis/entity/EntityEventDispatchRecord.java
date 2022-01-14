package com.didiglobal.mybatis.entity;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
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
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
@Getter
@Setter
public class EntityEventDispatchRecord implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 当前消息分发插件id
     */
    private String consumePluginName;

    /**
     * 消费消息唯一id，由logbook内部消费阶段产生。
     */
    private String consumeGlobalEventId;

    /**
     * 消费到的消息原始信息（记录下来、重试时使用）
     */
    private String consumeMessageContext;

    /**
     * 消息分发状态。0-未分发，1-分发完成
     */
    private Integer status;

    /**
     * 调用接口时产生的trace_id
     */
    private String traceId;

    /**
     * 调用接口时产生的span_id
     */
    private String spanId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 分发成功时间
     */
    private Date successTime;
    /**
     * 作废时间
     */
    private Date abortTime;
}
