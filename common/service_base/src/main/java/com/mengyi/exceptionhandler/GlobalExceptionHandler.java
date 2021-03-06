package com.mengyi.exceptionhandler;

import com.mengyi.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mengyiyouth
 * @date 2021/1/17 14:16
 * 统一异常处理
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定当你出现什么异常会执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    //特定异常
//    @ExceptionHandler(ArithmeticException.class)
//    @ResponseBody//为了返回数据
//    public R error(ArithmeticException e){
//        e.printStackTrace();
//        return R.error().message("执行了ArithmeticException异常处理");
//    }

    //自定义异常处理
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(e.getMsg());
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
