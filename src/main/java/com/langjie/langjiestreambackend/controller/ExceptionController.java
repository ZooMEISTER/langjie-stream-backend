package com.langjie.langjiestreambackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZooMEISTER
 * @Description: 全局异常处理
 * @DateTime 2024/8/9 14:56
 **/

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map globalException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        Map<String,Object> errMap = new HashMap<>();
        errMap.put("Code", 500);
        errMap.put("Message", e.getMessage());
        errMap.put("String", e.toString());
        errMap.put("StackTrace", e.getStackTrace());
        return errMap;
    }
}
