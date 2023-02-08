package com.searchify.suggestion.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class WebClientService {
    public String retrieve(final String baseUrl,
                           final Function<UriBuilder, URI> uriFunction,
                           final HttpMethod httpMethod,
                           final Map<String, List<String>> headers,
                           final List<MediaType> mediaTypes,
                           final Object body) {
        final WebClient webClient = WebClient.create(baseUrl);

        return webClient
                .method(httpMethod)
                .uri(uriFunction)
                .bodyValue(body)
                .headers(h -> h.putAll(headers))
                .accept(mediaTypes.toArray(MediaType[]::new))
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
