package com.didiglobal.mediator.translate;

import com.didiglobal.common.pojo.vo.SingleTranslateVo;
import com.didiglobal.common.translate.TranslateInput;
import com.didiglobal.mybatis.entity.TranslateRuleConfig;

/**
 * @author liyanling
 * @date 2021/11/15 4:36 下午
 */
public interface TranslateMediator {

    SingleTranslateVo<Object> translate(TranslateRuleConfig translateRuleConfig, TranslateInput translateInput);
}
