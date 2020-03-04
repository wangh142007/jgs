package com.wh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: latte
 * @description:
 * @author: wh
 * @create: 2020-01-13 14:02
 */
@SpringBootApplication
@MapperScan({"com.wh.mapper"})
@ComponentScan(basePackages = {"com.wh", "com.wh.org.n3r.idworker"})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
