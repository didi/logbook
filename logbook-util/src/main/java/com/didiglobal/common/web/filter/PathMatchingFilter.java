package com.didiglobal.common.web.filter;


import com.didiglobal.common.web.WebUtils;
import com.didiglobal.common.web.filterchain.matcher.AntPathMatcher;
import com.didiglobal.common.web.filterchain.matcher.PatternMatcher;
import com.didiglobal.common.web.processor.PathConfigProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.didiglobal.common.web.WebUtils.split;

/**
 * 对匹配uri的request进行filter
 * 不匹配的bypass
 */
public abstract class PathMatchingFilter extends AdviceFilter implements PathConfigProcessor {

    private static final Logger log = LoggerFactory.getLogger(PathMatchingFilter.class);

    protected PatternMatcher pathMatcher = new AntPathMatcher();

    /**
     * key: path
     * value: 对应path上当前filter实例应该使用的配置
     */
    protected Map<String, Object> appliedPaths = new LinkedHashMap<String, Object>();

    /**
     * 见PathConfigProcessor
     * config逗号分隔
     */
    public Filter processPathConfig(String path, String config) {
        String[] values = null;
        if (config != null) {
            values = split(config);
        }

        this.appliedPaths.put(path, values);
        return this;
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }

    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
        return pathsMatch(path, requestURI);
    }

    protected boolean pathsMatch(String pattern, String path) {
        return pathMatcher.matches(pattern, path);
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
            return true;
        }

        for (String path : this.appliedPaths.keySet()) {
            /**
             * 如果匹配，交给子类实现处理
             */
            if (pathsMatch(path, request)) {
                Object config = this.appliedPaths.get(path);
                return isFilterChainContinued(request, response, path, config);
            }
        }
        return true;
    }

    private boolean isFilterChainContinued(ServletRequest request, ServletResponse response,
                                           String path, Object pathConfig) throws Exception {

        if (isEnabled(request, response, path, pathConfig)) {
            // delegate to subclass
            return onPreHandle(request, response, pathConfig);
        }
        return true;
    }

    /**
     * 子类覆盖这个方法
     */
    protected boolean onPreHandle(ServletRequest request, ServletResponse response,
                                  Object mappedValue) throws Exception {
        return true;
    }

    protected boolean isEnabled(ServletRequest request, ServletResponse response, String path,
                                Object mappedValue)
            throws Exception {
        return isEnabled(request, response);
    }
}
