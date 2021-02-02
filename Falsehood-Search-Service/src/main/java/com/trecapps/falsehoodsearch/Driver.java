package com.trecapps.falsehoodsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Driver {

    public static void main(String[] args) {
        SpringApplication.run(Driver.class, args);
    }

}
