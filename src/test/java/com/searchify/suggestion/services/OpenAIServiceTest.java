package com.searchify.suggestion.services;

import com.searchify.config.test.SearchifyApplicationContextTestConfig;
import com.searchify.suggestion.api.request.CompletionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SearchifyApplicationContextTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class OpenAIServiceTest {
    @Autowired
    private OpenAIService openAIService;

    @MockBean
    private WebClientService webClientService;

    @Value("${integration.python.openai.base.url}")
    private String baseUrl;

    @Value("${integration.python.openai.text.path}")
    private String textPath;

    @Value("${integration.python.openai.text.httprequest.bodytemplate}")
    private String bodyTemplate;

    @Test
    void generateTextSuccess() throws InterruptedException {
        final CompletionRequest request = new CompletionRequest("test type", "finance", 25);
        final String body = String.format(bodyTemplate, request.getWrittenType(), request.getDomain(), request.getMaxTokens());
        final String mockResponse = "{\"suggestions\":\"This is indeed only mock response\"}";

        when(webClientService.retrieve(
                baseUrl,
                textPath,
                new LinkedMultiValueMap<>(),
                HttpMethod.POST,
                Map.of(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE)),
                List.of(MediaType.APPLICATION_JSON),
                body
        )).thenReturn(mockResponse);
        final String aiText = openAIService.generateText(request);

        assertEquals(mockResponse, aiText);
    }
}