package com.trecapps.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@ComponentScan(basePackages = {"com.trecapps.userservice"})
public class Driver
{
    public static void main(String[] args) {
        SpringApplication.run(Driver.class, args);
    }
}
