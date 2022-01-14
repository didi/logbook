package com.didiglobal.common.trace.info;

public class ExtElement {
    private String traceId = "";
    private String spanId = "";
    private String cspanId = "";

    public ExtElement() {
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return this.spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getCspanId() {
        return this.cspanId;
    }

    public void setCspanId(String cspanId) {
        this.cspanId = cspanId;
    }

}
