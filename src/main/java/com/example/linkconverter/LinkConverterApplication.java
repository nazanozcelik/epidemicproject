package com.example.linkconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = {"com.example.linkconverter"})
@EnableRetry
public class LinkConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkConverterApplication.class, args);
    }

}
