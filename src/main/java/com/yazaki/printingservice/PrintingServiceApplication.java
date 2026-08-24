package com.yazaki.printingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PrintingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrintingServiceApplication.class, args);
    }
}
