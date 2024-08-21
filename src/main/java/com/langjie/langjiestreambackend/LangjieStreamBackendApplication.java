package com.langjie.langjiestreambackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LangjieStreamBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(LangjieStreamBackendApplication.class, args);
    }

}
