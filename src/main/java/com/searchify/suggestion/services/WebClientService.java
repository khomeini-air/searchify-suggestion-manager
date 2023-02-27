package com.searchify.suggestion.services;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class WebClientService {
    public String retrieve(final String baseUrl,
                           final String path,
                           final MultiValueMap<String, String> params,
                           final HttpMethod httpMethod,
                           final Map<String, List<String>> headers,
                           final List<MediaType> mediaTypes,
                           final Object body) {
        final HttpClient httpClient = HttpClient.create()
                .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        final ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        WebClient webClient =  WebClient.builder().baseUrl(baseUrl)
                .clientConnector(connector)
                .build();

        return webClient
                .method(httpMethod)
                .uri(composeURIFunction(path, params))
                .bodyValue(body)
                .headers(h -> h.putAll(headers))
                .accept(mediaTypes.toArray(MediaType[]::new))
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private Function<UriBuilder, URI> composeURIFunction(final String path, final MultiValueMap<String, String> params) {
        return uriBuilder -> uriBuilder.path(path).queryParams(params).build();
    }
}
