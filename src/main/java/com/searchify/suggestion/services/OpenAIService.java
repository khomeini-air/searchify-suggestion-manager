package com.searchify.suggestion.services;

import com.searchify.suggestion.api.request.CompletionRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;

@Service
@Setter
public class OpenAIService {
    @Value("${integration.python.openai.base.url}")
    private String baseUrl;

    @Value("${integration.python.openai.text.path}")
    private String textPath;

    @Value("${integration.python.openai.text.httprequest.bodytemplate}")
    private String bodyTemplate;

    @Autowired
    private WebClientService webClientService;

    public String generateText(final CompletionRequest input) {
        return webClientService.retrieve(
                baseUrl,
                textPath,
                new LinkedMultiValueMap<>(),
                HttpMethod.POST,
                Map.of(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_JSON_VALUE)),
                List.of(MediaType.APPLICATION_JSON),
                genBodyText(input));
    }

    private String genBodyText(final CompletionRequest input) {
        final String result = String.format(bodyTemplate, input.getWrittenType(), input.getDomain(), input.getMaxTokens());
        return result;
    }
}
