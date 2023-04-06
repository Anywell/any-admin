package com.any;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.any.*.mapper")
@SpringBootApplication
public class AnyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnyAdminApplication.class, args);
    }

}
