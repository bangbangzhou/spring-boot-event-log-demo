package com.zbbmeta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author springboot葵花宝典
 * @description: TODO
 */
@SpringBootApplication
@EnableAsync//启用异步处理
public class EventListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventListenerApplication.class,args);
    }
}
