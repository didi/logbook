package com.didiglobal.common.error;


public enum AuthError implements ErrorNoMessage {
    AUTH_SYSTEM_ERROR(200001),
    NEED_AUTHZ(200002),
    ACCESS_DENY(200003),
    AUTHZ_FAILED(200004),
    ;

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

    private int errno;
    private String errmsg;

    AuthError(int errno) {
        this.errno = errno;
        this.errmsg = name();
    }
}