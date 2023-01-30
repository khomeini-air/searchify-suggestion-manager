package com.searchify.suggestion.services;

import com.searchify.config.test.SearchifyApplicationContextTestConfig;
import com.searchify.suggestion.entity.openai.CompletionRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SearchifyApplicationContextTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class OpenAIServiceTest {
    @Autowired
    private OpenAIService openAIService;

    private static MockWebServer webServer;

    @BeforeAll
    static void setUp() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        webServer.shutdown();
    }

    @Test
    void generateTextSuccess() throws InterruptedException {
        final CompletionRequest userRequest = new CompletionRequest("test type", "finance", 25);
        final String mockResponse = "{\"suggestions\":\"This is indeed only mock response\"}";

        openAIService.setBaseUrl(String.format("localhost:%s", webServer.getPort()));
        webServer.enqueue(new MockResponse().setBody(mockResponse).addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
        final String aiText = openAIService.generateText(userRequest);

        assertEquals(mockResponse, aiText);
    }
}