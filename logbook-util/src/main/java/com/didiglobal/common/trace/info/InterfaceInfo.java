package com.didiglobal.common.trace.info;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InterfaceInfo {

    private String localIp;
    private String remoteIp;
    private String interfaceName;
    private String serviceName;
    private Method method;
    private Object[] args;
    private Object output;
    private Throwable throwable;
    private Map<String, String> kvParams = new HashMap<String, String>();

    public InterfaceInfo() {
    }

    public InterfaceInfo(String interfaceName, String serviceName, Object[] args) {
        this.interfaceName = interfaceName;
        this.serviceName = serviceName;
        this.args = args;
    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object[] getArgs() {
        return args;
    }


    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public InterfaceInfo addKVParams(String key, String value) {
        kvParams.put(key, value);
        return this;
    }

    public InterfaceInfo addKVParams(Map<String, String> kvs) {
        kvParams.putAll(kvs);
        return this;
    }

    public Map<String, String> getKVParams() {
        return kvParams;
    }

    public void setKVParams(Map<String, String> kvParams) {
        this.kvParams = kvParams;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }


    @Override
    public String toString() {
        return "InterfaceInfo{" +
                "localIp='" + localIp + '\'' +
                ", remoteIp='" + remoteIp + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", method=" + method +
                ", args=" + Arrays.toString(args) +
                ", output=" + output +
                ", throwable=" + throwable +
                ", kvParams=" + kvParams +
                '}';
    }

}