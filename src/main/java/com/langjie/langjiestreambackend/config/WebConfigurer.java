package com.langjie.langjiestreambackend.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 11:02
 **/

@SpringBootConfiguration
public class WebConfigurer implements WebMvcConfigurer {

    /**
    * @Author: ZooMEISTER
    * @Description: 请求跨域配置
    * @DateTime: 2024/8/9 11:04
    * @Params: [corsRegistry]
    * @Return void
    */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        /**
         * 所有请求都允许跨域，使用这种配置就不需要
         * 在interceptor中配置header了
         */
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:4000")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(5000);
    }
}
