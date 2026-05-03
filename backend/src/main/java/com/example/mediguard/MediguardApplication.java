package com.example.mediguard;

import com.example.mediguard.domain.GeminiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GeminiConfig.class)
public class MediguardApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediguardApplication.class, args);
    }
}