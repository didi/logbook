package com.didiglobal.config.dbplugins;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
public interface DbTraceProcessor {
    void trace(DbInvocationContext context);
}
