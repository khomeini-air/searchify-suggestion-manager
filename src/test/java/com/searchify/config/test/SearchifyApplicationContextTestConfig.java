package com.searchify.config.test;

import com.searchify.suggestion.services.OpenAIService;
import com.searchify.suggestion.services.SemrushKeywordService;
import com.searchify.suggestion.services.SemrushTrafficService;
import com.searchify.suggestion.services.WebClientService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SearchifyApplicationContextTestConfig {
    @Bean
    public OpenAIService openAIService() {
        return new OpenAIService();
    }

    @Bean
    public SemrushTrafficService semrushTrafficService() {
        return new SemrushTrafficService();
    }

    @Bean
    public SemrushKeywordService semrushKeywordService() {
        return new SemrushKeywordService();
    }

    @Bean
    public WebClientService webClientService() {
        return new WebClientService();
    }
}
