package com.langjie.langjiestreambackend.annotation;

import java.lang.annotation.*;

/**
 * @Author ZooMEISTER
 * @Description: 接口日志声明
 * @DateTime 2024/8/9 11:34
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String description() default "";
}
