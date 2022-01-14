package com.didiglobal.common.pojo.vo;

import lombok.Data;

/**
 * @author mayingdong
 * @date 2021/11/15
 */
@Data
public class MqResult {
    /**
     * 返回码，0-正常，其他留作拓展
     */
    public int code;
    /**
     * 具体发送信息
     */
    public String msg;
    /**
     * 消息的key
     */
    public String key;
}
