package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.base.JSONResponse;
import org.example.exception.AppException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ControllerAdvice: 方法上可以使用@ExceptionHandler处理异常
 * @RestControllerAdvice: 由@ControllerAdvice和@ResponseBody组成
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j//使用lombok日志日志注解，之后使用log属性来完成日志打印
public class ExceptionAdvice {

    //自定义异常报错错误码和错误消息
    @ExceptionHandler(AppException.class)
    @ResponseBody
    public Object handle1(AppException e){
        JSONResponse json = new JSONResponse();
        json.setCode(e.getCode());
        json.setMessage(e.getMessage());
        log.debug("自定义异常", e);
        return json;
    }

    //非自定义异常（英文错误信息，堆栈信息，不能给用户看）：
    // 指定一个错误码，错误消息（未知错误，请联系管理员）
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handle2(Exception e){
        JSONResponse json = new JSONResponse();
        json.setCode("ERR000");
        json.setMessage("未知错误，请联系管理员");
        log.error("未知错误", e);
        return json;
    }
}
