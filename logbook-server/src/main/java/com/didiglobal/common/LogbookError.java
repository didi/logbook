package com.didiglobal.common;

import com.didiglobal.common.error.ErrorNoMessage;

/**
 * @author liyanling
 * @date 2021/10/26
 * todo clear useless code
 * todo errmsg use file config
 */
public enum LogbookError implements ErrorNoMessage {
    SUCCEED(1),
    PARAM_INVALID(100000 ),
    UNEXPECTED_ERROR(100001),

    UNSUPPORTED_PERMIT_TYPE(100002),
    UNSUPPORTED_TRANSLATE_TYPE(100003),
    UNSUPPORTED_FILTER_TYPE(100004),

    INSERT_AND_QUERY_FAILED(100005),

    FAILED_SERIALIZE_TO_STRING(100006),
    FAILED_DESERIALIZE_FROM_STRING(100007),
    FAILED_FILTER(100008),
    FAILED_DISPATCH(100009),
    FAILED_PERMIT(100010),

    NOT_FOUND_FILTER_RULE(100011),
    NOT_FOUND_PERMIT_MEDIATOR(100012),
    NOT_FOUND_PERMIT_RULE(100013),
    NOT_FOUND_TRANSLATE_RULE(100014),
    NOT_FOUND_REPORT_RECORD(100015),
    NOT_FOUND_DISPATCH_RECORD(100016),

    UNEXPECTED_NULL_RESULT_PERMIT(100020),
    UNEXPECTED_NULL_RESULT_TRANSLATE(100021),

    IN_PROCESSING_TRANSLATE_WAIT_RETRY(100022),

    ALREADY_REPORT_SUCCESS(100023),
    ALREADY_REPORT_ABANDON(100024),
    ALREADY_CONSUME_SUCCESS(100025),
    ALREADY_CONSUME_ABANDON(100026);


    private int errno;
    private String errmsg;

    LogbookError(int errno) {
        this.errno = errno;
        this.errmsg = name();
    }

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

}
