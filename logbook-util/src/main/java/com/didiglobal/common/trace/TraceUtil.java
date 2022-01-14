package com.didiglobal.common.trace;

import com.didiglobal.common.trace.info.LogMessage;
import com.didiglobal.common.trace.info.TraceEntry;
import com.didiglobal.common.trace.info.TraceLog;
import org.slf4j.MDC;

import java.util.UUID;

import static com.didiglobal.common.trace.info.TraceConstants.*;

public class TraceUtil {

    private static TraceLogProcessor LOG_PROCESSOR = new TraceLogProcessor();

    private final static ThreadLocal<TraceEntry> traceEntry = new ThreadLocal<TraceEntry>() {
        @Override
        protected TraceEntry initialValue() {
            return TraceEntry.create();
        }
    };

    public static TraceEntry getTraceEntry() {
        return traceEntry.get();
    }

    public static void removeTraceEntry() {
        traceEntry.remove();
        LogMessage.remove();
        MDC.clear();
    }


    public static String ensureTraceId(String traceId) {

        if (null == traceId || EMPTY.equals(traceId)) {
            traceId = genId();
        }

        traceEntry.get().setTraceId(traceId);

        MDC.put(MDC_TRACE_ID_KEY, traceId);

        return traceId;
    }

    public static String ensureSpanId(String spanId) {

        if (null == spanId || EMPTY.equals(spanId)) {
            spanId = genId();
        }

        traceEntry.get().setSpanId(spanId);

        MDC.put(MDC_SPAN_ID_KEY, spanId);

        return spanId;
    }

    public static void print(TraceLog log, boolean isWarnLog) {
        LOG_PROCESSOR.process(log, isWarnLog);
    }

    private static String genId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
