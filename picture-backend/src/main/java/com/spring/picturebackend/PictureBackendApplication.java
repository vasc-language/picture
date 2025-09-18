package com.spring.picturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动类
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.spring.picturebackend.mapper")
@SpringBootApplication
public class PictureBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(PictureBackendApplication.class, args);
    }
}
