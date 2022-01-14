package com.didiglobal.common.web.filterchain;


import com.didiglobal.common.web.filterchain.list.NamedFilterList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.Map;
import java.util.Set;

/**
 * filterChain 管理器
 */
public interface FilterChainManager {

    /**
     * 获取当前实例资源池内的filter
     * key: filter name（没有的话用classname）
     * value: filter instance
     */
    Map<String, Filter> getFilters();

    /**
     * 返回 chainName（通常是 uri） 对应的 filter 链
     * eg.
     * 想在 uri /** 上放一个 aFilter 和 bFilter
     * 此时 chainName 即为 "/**"
     * filterChain 即为 aFilter -> bFilter
     */
    NamedFilterList getChain(String chainName);

    boolean hasChains();

    Set<String> getChainNames();

    /**
     * 返回一个新的 FilterChain
     * 把 chainName 对应的 filterChain（如果有的话）挂到原始 chain 之前
     * 请求先过 chainName 对应的 filterChain，然后过原始 chain
     */
    FilterChain proxy(FilterChain original, String chainName);

    /**
     * 向资源池内加入新的 filter（到链尾）
     * 如果没有 filterName 对应的链应该先建链
     */
    void addFilter(String name, Filter filter);

    /**
     * 同 addFilter
     * init 用来标识是否加入时就用 servlet 容器的 filterConfig 对 filter 进行初始化
     */
    void addFilter(String name, Filter filter, boolean init);

    /**
     * 创建链，支持ant风格的通配符配置
     * eg.
     * 有如下定义
     * /**: trace
     * /api/login: rateLimiter[200]
     * <p>
     * 对应的 chainName 分别为 "/**" 和 "/api/login"
     * 对应的 chainDefinition 分别为 "trace" 和 "rateLimiter[200]"
     */
    void createChain(String chainName, String chainDefinition);

    /**
     * 把 filter append 到 chain 上
     */
    void addToChain(String chainName, String filterName);

    /**
     * 把 filter append 到 chain 上
     * 如果 filter 是一个 PathConfigProcessor 的话可以通过指定 chainSpecificFilterConfig 来定制 filter
     */
    void addToChain(String chainName, String filterName, String chainSpecificFilterConfig)
            throws ConfigurationException;
}