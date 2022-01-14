package com.didiglobal.common.trace;


import com.didiglobal.common.trace.info.LogMessage;
import com.didiglobal.common.trace.info.TraceLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

// non-public
class TraceLogProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceLogProcessor.class);

    public void process(TraceLog log, boolean isWarnLog) {
        try {
            if (null == log) {
                return;
            }

            String trLogStr = (genLogMessage(log)).toString();

            if (null == log.getFailureException()) {
                LOGGER.info(trLogStr);
                return;
            }

            if (isWarnLog) {
                LOGGER.warn(trLogStr, log.getFailureException());
            } else {
                LOGGER.error(trLogStr, log.getFailureException());
            }
        } catch (Throwable t) {
            LOGGER.error("caught error when process trace log|traceLog={}|error=", log, t);
        }
    }

    protected LogMessage genLogMessage(final TraceLog traceLog) {
        LogMessage logMessage = new LogMessage();

        //set tag
        if (StringUtils.isNotBlank(traceLog.getTag())) {
            logMessage.setDltag(traceLog.getTag());
        }

        //set traceid
        logMessage.setTraceId(TraceUtil.getTraceEntry().getTraceId());

        //set spanid
        logMessage.setSpanId(TraceUtil.getTraceEntry().getSpanId());

        //set childspanid
        if (StringUtils.isNotBlank(traceLog.getChildSpanId())) {
            logMessage.setCspanId(traceLog.getChildSpanId());
        }

        logMessage.add("service", traceLog.getServiceName());
        logMessage.add("interface", traceLog.getInterfaceName());
        logMessage.add("success", traceLog.isSuccess());
        logMessage.add("costTime", traceLog.getCost());
        logMessage.add("clientIp", traceLog.getClientIp());
        logMessage.add("timestamp", traceLog.getTimestamp());
        logMessage.add("remoteIp", traceLog.getRemoteIp());
        logMessage.add("headers", traceLog.getHeaders());

        if (traceLog.getKvParams() != null) {
            for (Map.Entry<String, String> entry : traceLog.getKvParams().entrySet()) {
                logMessage.add(entry.getKey(), entry.getValue());
            }
        }

        if (traceLog.getRpcMethodArgs() != null) {
            logMessage.add("rpcMethodArgs", traceLog.getRpcMethodArgs());
        }

        if (traceLog.getRpcMethodResult() != null) {
            logMessage.add("rpcMethodResult", traceLog.getRpcMethodResult());
        }

        return logMessage;
    }

}