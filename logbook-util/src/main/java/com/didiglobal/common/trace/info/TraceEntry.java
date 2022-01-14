package com.didiglobal.common.trace.info;

public class TraceEntry {

    public static TraceEntry create() {
        return new TraceEntry();
    }

    // 整条链路不变
    private String traceId;

    // 上游的 cspanid, 获取不到的话由处理请求的当前模块生成
    private String spanId;

    // 调下游时生成的 cspanid
    private String cspanId;

    private long initTimeMs = System.currentTimeMillis();

    private InterfaceInfo interfaceInfo;

    public String getTraceId() {
        return traceId;
    }

    public TraceEntry setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getSpanId() {
        return spanId;
    }

    public TraceEntry setSpanId(String spanId) {
        this.spanId = spanId;
        return this;
    }

    public String getCspanId() {
        return cspanId;
    }

    public TraceEntry setCspanId(String cspanId) {
        this.cspanId = cspanId;
        return this;
    }

    public long getInitTimeMs() {
        return initTimeMs;
    }

    public TraceEntry setInitTimeMs(long initTimeMs) {
        this.initTimeMs = initTimeMs;
        return this;
    }

    public InterfaceInfo getInterfaceInfo() {
        return interfaceInfo;
    }

    public TraceEntry setInterfaceInfo(InterfaceInfo interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TraceEntry{");
        sb.append("traceId='").append(traceId).append('\'');
        sb.append(", spanId='").append(spanId).append('\'');
        sb.append(", cspanId='").append(cspanId).append('\'');
        sb.append(", initTimeMs=").append(initTimeMs);
        sb.append(", interfaceInfo=").append(interfaceInfo);
        sb.append('}');
        return sb.toString();
    }
}