package com.searchify.suggestion.services;

import com.searchify.suggestion.api.request.CompletionRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Service
@Setter
public class OpenAIService {
    @Value("${integration.python.openai.base.url}")
    private String baseUrl;

    @Value("${integration.python.openai.text.path}")
    private String textPath;

    @Value("${integration.python.openai.text.httprequest.bodytemplate}")
    private String bodyTemplate;

    public String generateText(final CompletionRequest input) {
        final WebClient client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // Preparing request
        final UriSpec<RequestBodySpec> uriSpec = client.post();
        final RequestBodySpec bodySpec = uriSpec.uri(textPath);
        final RequestHeadersSpec headersSpec = bodySpec.bodyValue(genBodyText(input));

        // Execute the request and return
        final ResponseSpec responseSpec = headersSpec
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();

        final Mono<String> response = responseSpec.bodyToMono(String.class);
        return response.block();
    }

    private String genBodyText(final CompletionRequest input) {
        final String result = String.format(bodyTemplate, input.getWrittenType(), input.getDomain(), input.getMaxTokens());
        return result;
    }
}
