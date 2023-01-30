package com.searchify.config.test;

import com.searchify.suggestion.services.OpenAIService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SearchifyApplicationContextTestConfig {
    @Bean
    public OpenAIService openAIService() {
        return new OpenAIService();
    }
}
