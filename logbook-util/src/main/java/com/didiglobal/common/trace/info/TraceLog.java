package com.didiglobal.common.trace.info;


import java.util.Map;

public class TraceLog {

    public String tag;

    public String serviceName;

    public String interfaceName;

    public boolean success;

    /**
     * 必须是正数
     */
    public int cost;

    public String remoteIp;

    public String clientIp;

    /**
     * ms
     */
    public long timestamp;

    //public String failureReason;

    public Throwable failureException;

    public String rpcMethodArgs;

    public Map<String, String> kvParams;

    public void setRpcMethodResult(String rpcMethodResult) {
        this.rpcMethodResult = rpcMethodResult;
    }

    public String rpcMethodResult;

    public String childSpanId;

    public Map<String, String> headers;

    public String getTag() {
        return tag;
    }

    public TraceLog setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public TraceLog setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public TraceLog setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public TraceLog setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public TraceLog setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public TraceLog setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public TraceLog setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TraceLog setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Throwable getFailureException() {
        return failureException;
    }

    public TraceLog setFailureException(Throwable failureException) {
        this.failureException = failureException;
        return this;
    }

    public String getRpcMethodArgs() {
        return rpcMethodArgs;
    }

    public TraceLog setRpcMethodArgs(String rpcMethodArgs) {
        this.rpcMethodArgs = rpcMethodArgs;
        return this;
    }

    public Map<String, String> getKvParams() {
        return kvParams;
    }

    public TraceLog setKvParams(Map<String, String> kvParams) {
        this.kvParams = kvParams;
        return this;
    }

    public String getRpcMethodResult() {
        return rpcMethodResult;
    }

    public String getChildSpanId() {
        return childSpanId;
    }

    public TraceLog setChildSpanId(String childSpanId) {
        this.childSpanId = childSpanId;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public TraceLog setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(" [service=");
        sb.append(serviceName)
                .append(", interface=").append(interfaceName)
                .append(", success=").append(success)
                .append(", costTime=").append(cost)
                .append(", clientIp=").append(clientIp)
                .append(", remoteIp=").append(remoteIp)
                .append(", timestamp=").append(timestamp)
                .append(", headers=").append(headers);
        if (rpcMethodArgs != null) {
            sb.append(", rpcMethodArgs=").append(rpcMethodArgs);
        }

        if (rpcMethodResult != null) {
            sb.append(", rpcMethodResult=").append(rpcMethodResult);
        }
        sb.append("]");
        return sb.toString();
    }
}
