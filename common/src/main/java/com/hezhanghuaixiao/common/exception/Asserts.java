package com.hezhanghuaixiao.common.exception;


import com.hezhanghuaixiao.common.api.IErrorCode;

/*
 @describe 断言处理类，用于抛出各种API异常
 @params
 @return
 @author 何章怀晓
 @date 2020/8/24  9:57
 @other
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
