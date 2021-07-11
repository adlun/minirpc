package com.adlun.minirpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.adlun.minirpc",
//        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.adlun\\.minirpc\\.consumer")
//        })
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
