package com.didiglobal.mybatis.provider;

import com.didiglobal.mybatis.entity.EntityEventDispatchRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class EntityEventDispatchRecordProvider {

    private final String TABLE_NAME = "record_entity_event_progress";

    // 插入
    public String insert(@Param("record") EntityEventDispatchRecord record) {

        SQL sql = new SQL();
        sql.INSERT_INTO(TABLE_NAME);

        sql.VALUES("consume_plugin_name","#{consumePluginName}");
        sql.VALUES("consume_global_event_id","#{consumeGlobalEventId}");
        sql.VALUES("consume_message_context","#{consumeMessageContext}");

        sql.VALUES("status","#{status}");
        sql.VALUES("trace_id","#{traceId}");
        sql.VALUES("span_id","#{spanId}");
        sql.VALUES("create_time","#{createTime}");

        return sql.toString();
    }

    // 更新
    public String update(@Param("record") EntityEventDispatchRecord record,@Param("oldStatus") int oldStatus) {
        SQL sql = new SQL();
        sql.UPDATE(TABLE_NAME);
        if(record.getStatus() != null) {
            sql.SET("status = #{record.status}");
        }

        if(record.getTraceId() != null) {
            sql.SET("trace_id = #{record.traceId}");
        }
        if(record.getSpanId() != null) {
            sql.SET("span_id = #{record.spanId}");
        }
        if(record.getSuccessTime() != null) {
            sql.SET("success_time = #{record.successTime}");
        }
        if(record.getAbortTime() != null) {
            sql.SET("abort_time = #{record.abortTime}");
        }

//        sql.WHERE("consume_plugin_name="+record.getC
        sql.WHERE("consume_plugin_name = #{record.consumePluginName}");
        sql.WHERE("consume_global_event_id = #{record.consumeGlobalEventId}");
        sql.WHERE("status = #{oldStatus}");

        return sql.toString();
    }
}
