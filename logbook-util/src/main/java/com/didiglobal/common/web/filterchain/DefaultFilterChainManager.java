package com.didiglobal.common.web.filterchain;


import com.didiglobal.common.web.filter.Nameable;
import com.didiglobal.common.web.filterchain.list.NamedFilterList;
import com.didiglobal.common.web.filterchain.list.SimpleNamedFilterList;
import com.didiglobal.common.web.processor.PathConfigProcessor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.*;

/**
 * filter chain manager默认实现
 * 目前主要用在spring托管场景，也可自行定制
 */
public class DefaultFilterChainManager implements FilterChainManager {

    private static transient final Logger log = LoggerFactory.getLogger(DefaultFilterChainManager.class);

    // properties
    private FilterConfig filterConfig;
    private Map<String, Filter> filters; //key：filterName，value：filter instance
    private Map<String, NamedFilterList> filterChains; //key：filterChainName，value：filter instance list

    // constructor
    public DefaultFilterChainManager() {
        this.filters = new LinkedHashMap<String, Filter>();
        this.filterChains = new LinkedHashMap<String, NamedFilterList>();
    }

    public DefaultFilterChainManager(FilterConfig filterConfig) {
        this.filters = new LinkedHashMap<String, Filter>();
        this.filterChains = new LinkedHashMap<String, NamedFilterList>();
        setFilterConfig(filterConfig);
    }

    /**
     * 返回servlett容器的filterConfig
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * 应用启动时设置servlett容器的filterConfig
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Map<String, Filter> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Filter> filters) {
        this.filters = filters;
    }

    public Map<String, NamedFilterList> getFilterChains() {
        return filterChains;
    }

    public void setFilterChains(Map<String, NamedFilterList> filterChains) {
        this.filterChains = filterChains;
    }

    public Filter getFilter(String name) {
        return this.filters.get(name);
    }

    public void addFilter(String name, Filter filter) {
        addFilter(name, filter, false);
    }

    public void addFilter(String name, Filter filter, boolean init) {
        addFilter(name, filter, init, true);
    }

    public void createChain(String chainName, String chainDefinition) {
        if (StringUtils.isBlank(chainName)) {
            throw new NullPointerException("chainName cannot be null or empty.");
        }
        if (StringUtils.isBlank(chainDefinition)) {
            throw new NullPointerException("chainDefinition cannot be null or empty.");
        }

        if (log.isDebugEnabled()) {
            log.debug(
                    "Creating chain [" + chainName + "] from String definition [" + chainDefinition + "]");
        }

        //parse the value by tokenizing it to get the resulting filter-specific config entries
        //
        //e.g. for a value of
        //
        //     "authc, roles[admin,user], perms[file:edit]"
        //
        // the resulting token array would equal
        //
        //     { "authc", "roles[admin,user]", "perms[file:edit]" }
        //
        String[] filterTokens = splitChainDefinition(chainDefinition);

        //each token is specific to each filter.
        //strip the name and extract any filter-specific config between brackets [ ]
        for (String token : filterTokens) {
            String[] nameConfigPair = toNameConfigPair(token);

            //now we have the filter name, path and (possibly null) path-specific config.  Let's apply them:
            addToChain(chainName, nameConfigPair[0], nameConfigPair[1]);
        }
    }

    /**
     * Splits the comma-delimited filter chain definition line into individual filter definition tokens.
     * <p>
     * Example:
     * <p>
     * Input: foo, bar[baz], blah[x, y]
     * <p>
     * Output:
     * output[0] == foo
     * output[1] == bar[baz]
     * output[2] == blah[x, y]
     * <p>
     * 主要用在解析某个endpoint上挂了哪些filter及filter的配置
     */
    protected String[] splitChainDefinition(String chainDefinition) {
        return split(chainDefinition, ',', '[', ']', true, true);
    }

    public static String[] split(String aline, char delimiter, char beginquotechar, char endquotechar,
                                 boolean retainquotes, boolean trimtokens) {
        String line = StringUtils.trim(aline);
        if (line == null) {
            return null;
        } else {
            List<String> tokens = new ArrayList();
            StringBuilder sb = new StringBuilder();
            boolean inQuotes = false;

            for (int i = 0; i < line.length(); ++i) {
                char c = line.charAt(i);
                if (c == beginquotechar) {
                    if (inQuotes && line.length() > i + 1 && line.charAt(i + 1) == beginquotechar) {
                        sb.append(line.charAt(i + 1));
                        ++i;
                    } else {
                        inQuotes = !inQuotes;
                        if (retainquotes) {
                            sb.append(c);
                        }
                    }
                } else if (c == endquotechar) {
                    inQuotes = !inQuotes;
                    if (retainquotes) {
                        sb.append(c);
                    }
                } else if (c == delimiter && !inQuotes) {
                    String s = sb.toString();
                    if (trimtokens) {
                        s = s.trim();
                    }

                    tokens.add(s);
                    sb = new StringBuilder();
                } else {
                    sb.append(c);
                }
            }

            String s = sb.toString();
            if (trimtokens) {
                s = s.trim();
            }

            tokens.add(s);
            return tokens.toArray(new String[tokens.size()]);
        }
    }

    /**
     * Based on the given filter chain definition token (e.g. 'foo' or 'foo[bar, baz]'), this will return the token
     * as a name/value pair, removing any brackets as necessary.  Examples:
     * <p>
     * input: foo
     * output: {foo, null}
     * <p>
     * input: foo[bar, baz]
     * output: {foo, {bar, baz}}
     * <p>
     * 主要用在解析filter配置
     */
    protected String[] toNameConfigPair(String token) throws ConfigurationException {

        try {
            String[] pair = token.split("\\[", 2);
            String name = StringUtils.trim(pair[0]);

            if (name == null) {
                throw new IllegalArgumentException(
                        "Filter name not found for filter chain definition token: " + token);
            }
            String config = null;

            if (pair.length == 2) {
                config = StringUtils.trim(pair[1]);
                //if there was an open bracket, it assumed there is a closing bracket, so strip it too:
                config = config.substring(0, config.length() - 1);
                config = StringUtils.trim(config);

                //backwards compatibility prior to implementing SHIRO-205:
                //prior to SHIRO-205 being implemented,
                // it was common for end-users to quote the config inside brackets
                //if that config required commas.
                // We need to strip those quotes to get to the interior quoted definition
                //to ensure any existing quoted definitions still function for end users:
                if (config != null && config.startsWith("\"") && config.endsWith("\"")) {
                    String stripped = config.substring(1, config.length() - 1);
                    stripped = StringUtils.trim(stripped);

                    //if the stripped value does not have any internal quotes, we can assume that the entire config was
                    //quoted and we can use the stripped value.
                    if (stripped != null && stripped.indexOf('"') == -1) {
                        config = stripped;
                    }
                    //else:
                    //the remaining config does have internal quotes, so we need to assume that each comma delimited
                    //pair might be quoted, in which case we need the leading and trailing quotes that we stripped
                    //So we ignore the stripped value.
                }
            }
            return new String[]{name, config};
        } catch (Exception e) {
            String msg = "Unable to parse filter chain definition token: " + token;
            throw new ConfigurationException(msg, e);
        }
    }

    protected void addFilter(String name, Filter filter, boolean init, boolean overwrite) {
        Filter existing = getFilter(name);
        if (existing == null || overwrite) {
            if (filter instanceof Nameable) {
                ((Nameable) filter).setName(name);
            }
            if (init) {
                initFilter(filter);
            }
            this.filters.put(name, filter);
        }
    }

    public void addToChain(String chainName, String filterName) {
        addToChain(chainName, filterName, null);
    }

    public void addToChain(String chainName, String filterName, String chainSpecificFilterConfig) {
        if (StringUtils.isBlank(chainName)) {
            throw new IllegalArgumentException("chainName cannot be null or empty.");
        }
        Filter filter = getFilter(filterName);
        if (filter == null) {
            throw new IllegalArgumentException("There is no filter with name '" + filterName +
                    "' to apply to chain [" + chainName
                    + "] in the pool of available Filters.  Ensure a " +
                    "filter with that name/path has first been registered with the addFilter method(s).");
        }

        applyChainConfig(chainName, filter, chainSpecificFilterConfig);

        NamedFilterList chain = ensureChain(chainName);
        chain.add(filter);
    }

    protected void applyChainConfig(String chainName, Filter filter,
                                    String chainSpecificFilterConfig) {
        if (log.isDebugEnabled()) {
            log.debug("Attempting to apply path [" + chainName + "] to filter [" + filter + "] " +
                    "with config [" + chainSpecificFilterConfig + "]");
        }
        if (filter instanceof PathConfigProcessor) {
            ((PathConfigProcessor) filter).processPathConfig(chainName, chainSpecificFilterConfig);
        } else {
            if (StringUtils.isNotBlank(chainSpecificFilterConfig)) {
                //they specified a filter configuration, but the Filter doesn't implement PathConfigProcessor
                //this is an erroneous config:
                String msg = "chainSpecificFilterConfig was specified, but the underlying " +
                        "Filter instance is not an 'instanceof' " +
                        PathConfigProcessor.class.getName()
                        + ".  This is required if the filter is to accept " +
                        "chain-specific configuration.";
                throw new ConfigurationException(msg);
            }
        }
    }

    protected NamedFilterList ensureChain(String chainName) {
        NamedFilterList chain = getChain(chainName);
        if (chain == null) {
            chain = new SimpleNamedFilterList(chainName);
            this.filterChains.put(chainName, chain);
        }
        return chain;
    }

    public NamedFilterList getChain(String chainName) {
        return this.filterChains.get(chainName);
    }

    public boolean hasChains() {
        return !MapUtils.isEmpty(filterChains);
    }

    public Set<String> getChainNames() {
        //noinspection unchecked
        return this.filterChains != null ? this.filterChains.keySet() : Collections.EMPTY_SET;
    }

    public FilterChain proxy(FilterChain original, String chainName) {
        NamedFilterList configured = getChain(chainName);
        if (configured == null) {
            String msg = "There is no configured chain under the name/key [" + chainName + "].";
            throw new IllegalArgumentException(msg);
        }
        return configured.proxy(original);
    }

    /**
     * 用 servlet 容器的 filterConfig 初始化被托管的 filter 实例
     */
    protected void initFilter(Filter filter) {
        FilterConfig filterConfig = getFilterConfig();
        if (filterConfig == null) {
            throw new IllegalStateException(
                    "FilterConfig attribute has not been set.  This must occur before filter " +
                            "initialization can occur.");
        }
        try {
            filter.init(filterConfig);
        } catch (ServletException e) {
            throw new ConfigurationException(e);
        }
    }


}
