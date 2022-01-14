package com.didiglobal.common.trace.info;

public class LogKV {
    private String key;
    private String value;

    public LogKV(String key, Object value) {
        this.key = key;
        if (value == null) {
            value = "";
        }

        this.value = value.toString();
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
