package com.didiglobal.mybatis.provider;

import com.didiglobal.mybatis.entity.BizEventProcessRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class BizEventProcessRecordProvider {

    private final String TABLE_NAME = "record_biz_event_progress";

    // 插入
    public String insert(@Param("record") BizEventProcessRecord record) {

        SQL sql = new SQL();
        sql.INSERT_INTO(TABLE_NAME);

        sql.VALUES("global_biz_event_id","#{globalBizEventId}");

        sql.VALUES("status","#{status}");
        sql.VALUES("caller_name","#{callerName}");
        sql.VALUES("trace_id","#{traceId}");
        sql.VALUES("span_id","#{spanId}");
        sql.VALUES("biz_event_body","#{bizEventBody}");
        sql.VALUES("create_time","#{createTime}");

        return sql.toString();
    }

    // 更新
    public String update(@Param("record") BizEventProcessRecord record,@Param("oldStatus") int oldStatus) {
        SQL sql = new SQL();
        sql.UPDATE(TABLE_NAME);
        if(record.getStatus() != null) {
            sql.SET("status = #{record.status}");
        }
        if(record.getCallerName() != null) {
            sql.SET("caller_name = #{record.callerName}");
        }
        if(record.getTraceId() != null) {
            sql.SET("trace_id = #{record.traceId}");
        }
        if(record.getSpanId() != null) {
            sql.SET("span_id = #{record.spanId}");
        }
        if(record.getBizEventBody() != null) {
            sql.SET("biz_event_body = #{record.bizEventBody}");
        }
        if(record.getSuccessTime() != null) {
            sql.SET("success_time = #{record.successTime}");
        }
        if(record.getAbortTime() != null) {
            sql.SET("abort_time = #{record.abortTime}");
        }

        sql.WHERE("global_biz_event_id = #{record.globalBizEventId}");
        sql.WHERE("status = #{oldStatus}");
        return sql.toString();
    }
}
