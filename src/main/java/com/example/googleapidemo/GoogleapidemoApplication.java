package com.example.googleapidemo;

import com.example.googleapidemo.services.GoogleDirectoryService;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GoogleapidemoApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(GoogleapidemoApplication.class);

    private final GoogleDirectoryService googleDirectoryService;

    public GoogleapidemoApplication(GoogleDirectoryService googleDirectoryService) {
        this.googleDirectoryService = googleDirectoryService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GoogleapidemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            final var user = googleDirectoryService.findUser("someone");
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(user));
        } catch (IOException e) {
            logger.info("Not found: {}", e.getMessage());
        }
    }
}
