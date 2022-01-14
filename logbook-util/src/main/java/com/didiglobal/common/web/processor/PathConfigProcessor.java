package com.didiglobal.common.web.processor;

import javax.servlet.Filter;

/**
 * 提供配置filter的能力
 */
public interface PathConfigProcessor {

    /**
     * path: config应用到的path
     * config: 配置filter的config
     */
    Filter processPathConfig(String path, String config);
}
