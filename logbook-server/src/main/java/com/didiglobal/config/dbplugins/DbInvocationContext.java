package com.didiglobal.config.dbplugins;

/**
 * @author mayingdong
 * @date 2021/11/3
 */
public class DbInvocationContext {

    private String statementId;// eg: com.xiaoju.sail.keel.mybatis.user.UserMapper.queryById
    private String sql;// eg: select * from `user` where id=?
    private Object paramObject;// eg:{"id":"6"}

    private String interfaceName;// 从 statementId 截取部分。eg: UserMapper.queryById

    private Throwable failure;// 异常
    private long cost;// 耗时
    private Object result;// 返回值

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getParamObject() {
        return paramObject;
    }

    public void setParamObject(Object paramObject) {
        this.paramObject = paramObject;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Throwable getFailure() {
        return failure;
    }

    public void setFailure(Throwable failure) {
        this.failure = failure;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DbInvocationContext{" + "statementId='" + statementId + '\'' + ", sql='" + sql + '\'' + ", paramObject="
                + paramObject + ", interfaceName='" + interfaceName + '\'' + ", failure=" + failure + ", cost=" + cost
                + ", result=" + result + '}';
    }
}
