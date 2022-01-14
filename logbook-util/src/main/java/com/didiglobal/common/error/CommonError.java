package com.didiglobal.common.error;

public enum CommonError implements ErrorNoMessage {
    SUCCESS(0);

    private int errno;
    private String errmsg;

    @Override
    public int getErrno() {
        return errno;
    }

    @Override
    public String getErrmsg() {
        if (null == errmsg) {
            return name();
        }
        return errmsg;
    }

    CommonError(int errno) {
        this.errno = errno;
        this.errmsg = name();
    }

    CommonError(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }
}