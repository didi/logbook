package com.didiglobal.common.response;

public interface ApiResponse<T> {
    int getErrno();

    ApiResponse setErrno(int errno);

    String getErrmsg();

    ApiResponse setErrmsg(String errmsg);

    T getData();

    ApiResponse setData(T data);
}
