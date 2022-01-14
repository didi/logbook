package com.didiglobal.common.trace.info;

public class TraceConstants {
    public static final String _COM_REQUEST_IN = "_com_request_in";
    public static final String _COM_REQUEST_OUT = "_com_request_out";

    public static final String _COM_HTTP_SUCCESS = "_com_http_success";
    public static final String _COM_HTTP_FAILURE = "_com_http_failure";

    public static final String _COM_MYSQL_SUCCESS = "_com_mysql_success";
    public static final String _COM_MYSQL_FAILURE = "_com_mysql_failure";

    public static final String _COM_REDIS_SUCCESS = "_com_redis_success";
    public static final String _COM_REDIS_FAILURE = "_com_redis_failure";

    public static final String EMPTY = "";

    public static final String MDC_TRACE_ID_KEY = "traceId";

    public static final String MDC_SPAN_ID_KEY = "spanId";

    public static final String MDC_CSPAN_ID_KEY = "cspanId";


    public TraceConstants() {
    }
}
