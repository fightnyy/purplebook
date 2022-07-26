package com.purple.purplebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class PurpleBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurpleBookApplication.class, args);
    }

}
