package com.didiglobal.mediator.permit;

import com.didiglobal.common.param.BizEventParam;
import com.didiglobal.common.pojo.vo.PermitVo;
import com.didiglobal.mybatis.entity.PermitRuleConfig;

/**
 * @author liyanling
 * @date 2021/11/15 4:13 下午
 */
public interface PermitMediator {

    /**
     * 使用 permitRuleConfig 中的 permit_type 对应的准入方案实现
     * 对业务事件 bizEventParam 进行准入校验，最终返回带详情的校验结果
     *
     * @param bizEventParam  业务事件
     * @param permitRuleConfig 业务事件对应的准入配置
     * @return 业务事件准入结果
     */
    PermitVo<Object> permit(BizEventParam bizEventParam, PermitRuleConfig permitRuleConfig);

}
