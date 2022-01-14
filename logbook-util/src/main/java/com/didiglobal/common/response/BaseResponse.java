package com.didiglobal.common.response;

import com.didiglobal.common.error.ErrorNoMessage;

import static com.didiglobal.common.error.CommonError.SUCCESS;


@SuppressWarnings("unchecked")
public class BaseResponse<T> implements ApiResponse<T> {

    int errno;

    String errmsg;

    T data;

    // constructor s
    public BaseResponse(ErrorNoMessage errorNoMessage) {
        this.errno = errorNoMessage.getErrno();
        this.errmsg = errorNoMessage.getErrmsg();
    }

    public BaseResponse() {
        this(SUCCESS);
    }

    public BaseResponse(T data) {
        this();
        this.data = data;
    }
    public BaseResponse(ErrorNoMessage errorNoMessage,T data) {
        this();
        this.errno = errorNoMessage.getErrno();
        this.errmsg = errorNoMessage.getErrmsg();
        this.data = data;
    }


    // simple builder s
    public static BaseResponse make() {
        return new BaseResponse();
    }

    public static <T> BaseResponse<T> make(T data) {
        return new BaseResponse().setData(data);
    }

    public static BaseResponse makeError(ErrorNoMessage errorNoMessage) {
        return new BaseResponse(errorNoMessage);
    }

    public static <T> BaseResponse<T> makeErrorWithData(ErrorNoMessage errorNoMessage,T data) {
        return new BaseResponse(errorNoMessage,data);
    }



    // getter and setter
    @Override
    public int getErrno() {
        return errno;
    }

    @Override
    public ApiResponse setErrno(int errno) {
        this.errno = errno;
        return this;
    }

    @Override
    public String getErrmsg() {
        return errmsg;
    }

    @Override
    public ApiResponse setErrmsg(String errmsg) {
        this.errmsg = errmsg;
        return this;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public BaseResponse setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseResponse{");
        sb.append("errno=").append(errno);
        sb.append(", errmsg='").append(errmsg).append('\'');
        if (null != data) {
            sb.append(", data='").append(data).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}
