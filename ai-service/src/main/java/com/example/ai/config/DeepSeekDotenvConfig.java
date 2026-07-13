package com.example.ai.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DeepSeekDotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        Map<String, Object> properties = new HashMap<>();
        putIfPresent(dotenv, properties, "DEEPSEEK_API_KEY");
        putIfPresent(dotenv, properties, "DEEPSEEK_BASE_URL");
        putIfPresent(dotenv, properties, "DEEPSEEK_MODEL");

        if (!properties.isEmpty()) {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", properties));
        }
    }

    private static void putIfPresent(Dotenv dotenv, Map<String, Object> properties, String key) {
        String value = dotenv.get(key, null);
        if (value != null && !value.isBlank()) {
            properties.put(key, value);
        }
    }
}
