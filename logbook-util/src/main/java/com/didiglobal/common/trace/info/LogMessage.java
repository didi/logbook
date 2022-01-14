package com.didiglobal.common.trace.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogMessage {
    private static String DEFAULT_DLTAG = "_undef";
    private static ThreadLocal<ExtElement> extElements = new InheritableThreadLocal<ExtElement>() {
        protected ExtElement initialValue() {
            return new ExtElement();
        }
    };
    private String dltag = DEFAULT_DLTAG;
    private String cspanId = "";
    private List<LogKV> logElements = new ArrayList();

    public LogMessage() {
    }

    public static String getTraceId() {
        return ((ExtElement)extElements.get()).getTraceId();
    }

    public static String getSpanId() {
        return ((ExtElement)extElements.get()).getSpanId();
    }

    public static String getNewCspanId() {
        return ((ExtElement)extElements.get()).getCspanId();
    }

    public static void setTraceId(String traceId) {
        ((ExtElement)extElements.get()).setTraceId(traceId);
    }

    public static void setSpanId(String spanId) {
        ((ExtElement)extElements.get()).setSpanId(spanId);
    }

    public LogMessage setCspanId(String cspanId) {
        this.cspanId = cspanId;
        return this;
    }

    public LogMessage setDltag(String dltag) {
        this.dltag = dltag;
        return this;
    }

    public LogMessage add(String key, Object value) {
        LogKV logKV = new LogKV(key, value);
        this.logElements.add(logKV);
        return this;
    }


    public String getDltag() {
        return this.dltag != null && !this.dltag.equals("") ? this.dltag : DEFAULT_DLTAG;
    }

    public String getCspanId() {
        return this.cspanId;
    }

    public static void remove() {
        extElements.remove();
    }

    public static void setExtElements(ThreadLocal<ExtElement> extElements) {
        LogMessage.extElements = extElements;
    }

    public String toString() {
        ExtElement extElement = (ExtElement)extElements.get();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getDltag());
        sb.append("||traceid=");
        sb.append(extElement.getTraceId());
        sb.append("||spanid=");
        sb.append(extElement.getSpanId());
        sb.append("||cspanid=");
        sb.append(this.getCspanId());
        if (this.logElements.size() > 0) {
            Iterator var3 = this.logElements.iterator();

            while(var3.hasNext()) {
                LogKV param = (LogKV)var3.next();
                sb.append("||");
                sb.append(param.getKey());
                sb.append("=");
                sb.append(param.getValue());
            }
        }

        return sb.toString();
    }
}
