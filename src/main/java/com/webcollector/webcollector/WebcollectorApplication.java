package com.webcollector.webcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebcollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebcollectorApplication.class, args);
    }

}
