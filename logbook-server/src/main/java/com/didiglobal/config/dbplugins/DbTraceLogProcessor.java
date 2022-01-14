package com.didiglobal.config.dbplugins;

import com.alibaba.fastjson.JSONObject;
import com.didiglobal.common.trace.TraceUtil;
import com.didiglobal.common.trace.info.TraceLog;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@Slf4j
public class DbTraceLogProcessor implements DbTraceProcessor {

    public static final String SERVICE_NAME = "MySQL";
    public static final String MYSQL_SUCCESS_TAG = "_com_mysql_success";
    public static final String MYSQL_FAILURE_TAG = "_com_mysql_failure";


    @Override
    public void trace(DbInvocationContext context) {
        TraceUtil.print(genTRLog(context), false);
    }


    /**
     * 生成 TraceLog
     *
     * @param context
     * @return
     */
    private TraceLog genTRLog(DbInvocationContext context) {
        TraceLog traceLog = new TraceLog();
        try {
            Throwable throwable = context.getFailure();
            boolean success = null == throwable;

            traceLog.setServiceName(SERVICE_NAME);
            traceLog.setTag(success ? MYSQL_SUCCESS_TAG : MYSQL_FAILURE_TAG);
            traceLog.setFailureException(throwable);
            traceLog.setCost((int) context.getCost());
            traceLog.setSuccess(success);
            String params = JSONObject.toJSONString(context.getParamObject());
            // mapper.接口签名
            traceLog.setInterfaceName(context.getStatementId());
            // sql 参数
            traceLog.setRpcMethodArgs(context.getSql() + params);
            // 返回值
            traceLog.setRpcMethodResult(String.valueOf(context.getResult()));
            traceLog.setTimestamp(System.currentTimeMillis());
            return traceLog;
        } catch (Exception e) {
            log.error("genTRLog failed,trace={},traceLog={},e=", context, traceLog, e);
        }
        return traceLog;
    }

}
