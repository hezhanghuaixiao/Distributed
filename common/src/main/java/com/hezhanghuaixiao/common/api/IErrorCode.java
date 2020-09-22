package com.hezhanghuaixiao.common.api;

/*
 @describe 封装API的错误码
 @params
 @return
 @author 何章怀晓
 @date 2020/8/24  9:56
 @other
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
