package com.newsroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class NewsRoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsRoomApplication.class, args);
    }
}

