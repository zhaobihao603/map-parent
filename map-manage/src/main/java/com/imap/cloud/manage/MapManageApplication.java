package com.imap.cloud.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description TODO
 * @Author ZhaoBiHao
 * @Date 2019/1/9 17:24
 * @Version 2.0
 */
@SpringBootApplication
@EnableTransactionManagement//开启事务管理
@ComponentScan("com.imap.cloud")
@ServletComponentScan
@EnableFeignClients
@EnableHystrix
public class MapManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapManageApplication.class, args);
    }

}