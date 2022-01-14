package com.didiglobal.config.dbplugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisTraceInterceptor implements Interceptor {

    // 处理器列表。
    private List<DbTraceProcessor> processorList;

    public MybatisTraceInterceptor(List<DbTraceProcessor> processorList) {
        this.processorList = processorList;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        DbInvocationContext context = initContext(invocation);

        try {
            Object result = invocation.proceed();
            context.setCost(System.currentTimeMillis() - start);
            context.setResult(result);
            process(context);
            return result;
        } catch (Throwable t) {
            context.setCost(System.currentTimeMillis() - start);
            context.setFailure(t);
            process(context);
            throw t;
        }
    }


    @Override
    public Object plugin(Object o) {
        if (o instanceof Executor) {
            return Plugin.wrap(o, this);
        }
        return o;
    }

    @Override
    public void setProperties(Properties properties) {
    }


    /**
     * 初始化 MysqlInvocationContext
     *
     * @param invocation
     * @return
     */
    private DbInvocationContext initContext(Invocation invocation) {
        DbInvocationContext context = new DbInvocationContext();

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        // 获取参数，if 语句成立就表示sql语句有参数，参数格式是 map 形式
        Object parameterObject = null;
        if (invocation.getArgs().length > 1) {
            parameterObject = invocation.getArgs()[1];
        }
        context.setParamObject(parameterObject);

        // 获取到节点的 id，即 sql 语句的 id（是 mapper 接口方法签名）
        String statementId = mappedStatement.getId();
        context.setStatementId(statementId);

        // 从 statementId 截取字段
        String interfaceName = getInterfaceName(statementId);
        context.setInterfaceName(interfaceName);

        // BoundSql 是封装 myBatis 最终产生的 sql 类
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        context.setSql(sql);

        return context;
    }

    /**
     * 从 statementId 中取接口短名字，用于上报 metric 时使用。
     * 即：获取 "com.*.*Mapper.xxx" 的 "*Mapper.xxx" 这部分
     * 方法：找到倒数第二个 . 的位置，取其后面的部分
     *
     * @param statementId
     * @return
     */
    private String getInterfaceName(String statementId) {
        String[] arr = statementId.split("\\.");
        int size = arr.length;
        if (size < 2) {
            return statementId;
        }
        return arr[size - 2] + "." + arr[size - 1];
    }

    /**
     * 逐个处理
     *
     * @param context
     */
    private void process(DbInvocationContext context) {
        if (null == this.processorList || this.processorList.isEmpty()) {
            return;
        }
        for (DbTraceProcessor processor : this.processorList) {
            processor.trace(context);
        }
    }


}
