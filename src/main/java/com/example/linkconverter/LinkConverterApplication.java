package com.example.linkconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.example.linkconverter"})
@EnableSwagger2
public class LinkConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkConverterApplication.class, args);
    }

}
