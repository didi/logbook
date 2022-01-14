package com.didiglobal.common.trace.filter;


import com.didiglobal.common.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 记录请求耗时 ms
 */
public abstract class ProcessTimeFilter extends PathMatchingFilter {

    public static final String PROCESS_START_TIME_KEY = "process.start.time.ms";

    public static final String PROCESS_FINISH_TIME_KEY = "process.end.time.ms";

    public static final String PROCESS_DURATION_KEY = "process.duration.time.ms";

    protected long getProcessStartTime(ServletRequest request) {
        return request.getAttribute(PROCESS_START_TIME_KEY) == null
                ? 0
                : (long) request.getAttribute(PROCESS_START_TIME_KEY);
    }

    protected long getProcessFinishTime(ServletRequest request) {
        return request.getAttribute(PROCESS_FINISH_TIME_KEY) == null
                ? 0
                : (long) request.getAttribute(PROCESS_FINISH_TIME_KEY);
    }

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response,
                                  Object mappedValue) throws Exception {
        if (request.getAttribute(PROCESS_START_TIME_KEY) == null) {
            request.setAttribute(PROCESS_START_TIME_KEY, System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception)
            throws Exception {
        if (request.getAttribute(PROCESS_FINISH_TIME_KEY) == null) {
            long finishTimeMs = System.currentTimeMillis();
            long startTimeMs = (long) request.getAttribute(PROCESS_START_TIME_KEY);
            request.setAttribute(PROCESS_FINISH_TIME_KEY, System.currentTimeMillis());
            request.setAttribute(PROCESS_DURATION_KEY, finishTimeMs - startTimeMs);
        }
    }
}
