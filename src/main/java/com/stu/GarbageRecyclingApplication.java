package com.stu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.stu.mapper") // 扫描Mapper接口所在的包
public class GarbageRecyclingApplication {

    public static void main(String[] args) {
        SpringApplication.run(GarbageRecyclingApplication.class, args);
    }
}