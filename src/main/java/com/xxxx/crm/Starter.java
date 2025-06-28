package com.xxxx.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/27 19:22
 */
@SpringBootApplication
@MapperScan("com.xxxx.crm.dao")
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}
