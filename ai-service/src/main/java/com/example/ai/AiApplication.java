package com.example.ai;

import com.example.ai.config.DeepSeekDotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AiApplication.class);
        app.addInitializers(new DeepSeekDotenvConfig());
        app.run(args);
    }
}
