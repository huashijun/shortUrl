package com.huashijun.shorten;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.huashijun"})
public class ShortenApplication {

    public static void main(String[] args) {

        SpringApplication.run(ShortenApplication.class, args);
    }

}
