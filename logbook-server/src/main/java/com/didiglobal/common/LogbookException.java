package com.didiglobal.common;

import com.didiglobal.common.error.ErrorNoMessage;


/**
 * @author liyanling
 * @date 2021/10/26
 */
public class LogbookException extends RuntimeException implements ErrorNoMessage {

    private int errno;
    private String errmsg;
    private Object exceptionDetail;

    // constructor s
    public LogbookException(ErrorNoMessage errorNoMessage) {
        super(errorNoMessage.getErrmsg());
        this.errno = errorNoMessage.getErrno();
        this.errmsg = errorNoMessage.getErrmsg();
    }

    public LogbookException(ErrorNoMessage errorNoMessage, Object exceptionDetail) {
        super(errorNoMessage.getErrmsg());
        this.errno = errorNoMessage.getErrno();
        this.errmsg = errorNoMessage.getErrmsg();
        this.exceptionDetail = exceptionDetail;
    }

    public LogbookException() {
        super();
    }

    public LogbookException(String message) {
        super(message);
    }

    public LogbookException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogbookException(Throwable cause) {
        super(cause);
    }


    //  getter


    @Override
    public int getErrno() {
        return errno;
    }

    @Override
    public String getErrmsg() {
        return errmsg;
    }

    public Object getExceptionDetail() {
        return exceptionDetail;
    }

    // setter
    public LogbookException errorNoMessage(ErrorNoMessage errorNoMessage) {
        this.errno = errorNoMessage.getErrno();
        this.errmsg = errorNoMessage.getErrmsg();
        return this;
    }

    @Override
    public String toString() {
        return "LogbookException{" +
                "errno=" + errno +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }


//    @Override
//    public String getMessage() {
//        String msg = super.getMessage();
//        if (null == msg) {
//            return this.toString();
//        }
//        return msg;
//    }

}