package com.didiglobal.common.web.filter;

import com.didiglobal.common.web.filterchain.resolver.FilterChainResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class AbstractDelegatingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDelegatingFilter.class);


    // property
    private FilterChainResolver filterChainResolver;

    // constructor s
    public AbstractDelegatingFilter() {
    }

    public AbstractDelegatingFilter(FilterChainResolver filterChainResolver) {
        this.filterChainResolver = filterChainResolver;
    }

    // getter and setter
    public FilterChainResolver getFilterChainResolver() {
        return filterChainResolver;
    }

    public AbstractDelegatingFilter setFilterChainResolver(FilterChainResolver filterChainResolver) {
        this.filterChainResolver = filterChainResolver;
        return this;
    }


    @Override
    protected void doFilterInternal(ServletRequest request,
                                    ServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        Throwable t = null;
        try {
            executeChain(request, response, chain);
        } catch (Throwable throwable) {
            LOGGER.warn("caught error exec filter chain: ", t);
            t = throwable;
        }
        handleThrowable(t);
    }

    protected void executeChain(ServletRequest request,
                                ServletResponse response,
                                FilterChain origChain)
            throws IOException, ServletException {
        FilterChain chain = getExecutionChain(request, response, origChain);
        chain.doFilter(request, response);
    }


    protected FilterChain getExecutionChain(ServletRequest request,
                                            ServletResponse response,
                                            FilterChain origChain) {
        FilterChain chain = origChain;

        FilterChainResolver resolver = getFilterChainResolver();
        if (resolver == null) {
            return origChain;
        }

        FilterChain resolved = resolver.getChain(request, response, origChain);
        if (resolved != null) {
            chain = resolved;
        }

        return chain;
    }


    private void handleThrowable(Throwable t) throws ServletException, IOException {
        if (null == t) {
            return;
        }
        if (t instanceof ServletException) {
            throw (ServletException) t;
        }
        if (t instanceof IOException) {
            throw (IOException) t;
        }
        throw new ServletException("Filtered request failed.", t);
    }

}
