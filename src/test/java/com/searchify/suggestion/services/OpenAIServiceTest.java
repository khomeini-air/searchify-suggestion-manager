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

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Value("${integration.python.openai.text.httprequest.bodytemplate}")
    private String bodyTemplate;

    @Test
    void generateTextSuccess() throws InterruptedException {
        final CompletionRequest request = new CompletionRequest("test type", "finance", 25);
        final String body = String.format(bodyTemplate, request.getWrittenType(), request.getDomain(), request.getMaxTokens());
        final String mockResponse = "{\"suggestions\":\"This is indeed only mock response\"}";

        when(webClientService.retrieve(
                eq(baseUrl),
                any(Function.class),
                eq(HttpMethod.POST),
                eq(Map.of(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE))),
                eq(List.of(MediaType.APPLICATION_JSON)),
                eq(body)
        )).thenReturn(mockResponse);
        final String aiText = openAIService.generateText(request);

        assertEquals(mockResponse, aiText);
    }
}