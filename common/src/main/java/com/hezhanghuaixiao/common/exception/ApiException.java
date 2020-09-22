package com.hezhanghuaixiao.common.exception;


import com.hezhanghuaixiao.common.api.IErrorCode;

/*
 @describe 自定义API异常
 @params
 @return
 @author 何章怀晓
 @date 2020/8/24  9:57
 @other
 */
public class ApiException extends RuntimeException {
    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
