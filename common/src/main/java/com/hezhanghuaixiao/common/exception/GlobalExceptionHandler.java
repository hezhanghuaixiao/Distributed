package com.hezhanghuaixiao.common.exception;

import com.hezhanghuaixiao.common.api.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 @describe 全局异常处理
 @params
 @return
 @author 何章怀晓
 @date 2020/8/24  9:57
 @other
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseBody
  @ExceptionHandler(value = ApiException.class)
  public CommonResult handle(ApiException e) {
    if (e.getErrorCode() != null) {
      return CommonResult.failed(e.getErrorCode());
    }
    return CommonResult.failed(e.getMessage());
  }
}
