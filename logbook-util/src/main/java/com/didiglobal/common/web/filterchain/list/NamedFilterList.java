package com.didiglobal.common.web.filterchain.list;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.List;

/**
 * 一组能以name区分的filter（name通常是filter应该应用到的uri pattern）
 */
public interface NamedFilterList extends List<Filter> {

    String getName();

    /**
     * 输入原始链
     * 输出新链（ProxiedFilterChain）
     * request 在新链上执行，先过 NamedFilterList 的 filter，再过原始链
     */
    FilterChain proxy(FilterChain filterChain);
}
