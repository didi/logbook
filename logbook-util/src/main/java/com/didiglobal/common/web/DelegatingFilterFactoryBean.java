package com.didiglobal.common.web;

import com.didiglobal.common.web.filter.AbstractDelegatingFilter;
import com.didiglobal.common.web.filter.Nameable;
import com.didiglobal.common.web.filterchain.DefaultFilterChainManager;
import com.didiglobal.common.web.filterchain.FilterChainManager;
import com.didiglobal.common.web.filterchain.resolver.FilterChainResolver;
import com.didiglobal.common.web.filterchain.resolver.PathMatchingFilterChainResolver;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;


public class DelegatingFilterFactoryBean implements FactoryBean, BeanPostProcessor {

    private static transient final Logger LOGGER = LoggerFactory.getLogger(DelegatingFilterFactoryBean.class);

    private Map<String, Filter> filters;

    private Map<String, String> filterChainDefinitionMap;

    private AbstractDelegatingFilter instance;

    public DelegatingFilterFactoryBean() {
        /**
         * filterName - filterInstance
         */
        this.filters = new LinkedHashMap<String, Filter>();
        /**
         * chainName - filterNameListString
         * 对filter来说, apply的先后顺序非常重要!
         */
        this.filterChainDefinitionMap = new LinkedHashMap<String, String>();
    }

    public Map<String, Filter> getFilters() {
        return filters;
    }

    public DelegatingFilterFactoryBean setFilters(
            Map<String, Filter> filters) {
        this.filters = filters;
        return this;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public DelegatingFilterFactoryBean setFilterChainDefinitionMap(
            Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
        return this;
    }

    /**
     * 返回 bean 对象（实际是 DelegatingAllFilter 对象）
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() throws Exception {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    /**
     * 返回 bean 类型（即：DelegatingAllFilter）
     *
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return DelegatingAllFilter.class;
    }

    /**
     * 是否为单例 bean
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * bean 初始化前
     * 自动把扫描出来的 filter bean 加到 filters 里
     * 也就是说 filters 里出现的不仅是自定义的 filter，还有扫描出来的 filter
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof Filter) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found filter chain candidate filter '{}'", beanName);
            }
            Filter filter = (Filter) bean;
            getFilters().put(beanName, filter);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Ignoring non-Filter bean '{}'", beanName);
            }
        }
        return bean;
    }

    /**
     * 初始化后，啥也不做
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 创建 DelegatingAllFilter 实例
     *
     * @return
     * @throws Exception
     */
    protected DelegatingAllFilter createInstance() throws Exception {
        FilterChainManager manager = createFilterChainManager();
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);

        return new DelegatingAllFilter(chainResolver);
    }


    protected FilterChainManager createFilterChainManager() {

        DefaultFilterChainManager manager = new DefaultFilterChainManager();

        // 用 filters 初始化 FilterChainManager
        Map<String, Filter> filters = getFilters();
        if (!MapUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }
                /**
                 * init 设成false
                 * 让spring管理bean的初始化
                 */
                manager.addFilter(name, filter, false);
            }
        }

        // 用 filterChainDefinitionMap 更新 FilterChainManager 的 NamedFilterList
        Map<String, String> chains = getFilterChainDefinitionMap();
        if (!MapUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                manager.createChain(url, chainDefinition);
            }
        }

        return manager;
    }

    public static final class DelegatingAllFilter extends AbstractDelegatingFilter {

        public DelegatingAllFilter(FilterChainResolver resolver) {
            if (null == resolver) {
                throw new IllegalArgumentException("resolver cannot be null.");
            }
            super.setFilterChainResolver(resolver);
        }
    }
}
