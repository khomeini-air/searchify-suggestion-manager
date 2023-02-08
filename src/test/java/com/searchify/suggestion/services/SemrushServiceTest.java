package com.searchify.suggestion.services;

import com.searchify.config.test.SearchifyApplicationContextTestConfig;
import com.searchify.suggestion.entity.semrush.request.SemrushOrganicCompetitorRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushOrganicCompetitorResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSummaryResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SearchifyApplicationContextTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class SemrushServiceTest {
    @Autowired
    private SemrushService semrushService;

    @MockBean
    private WebClientService webClientService;

    @Value("${integration.semrush.baseurl}")
    private String baseUrl;

    @Value("${integration.semrush.api.baseurl}")
    private String apiBaseUrl;

    @Test
    void getApiUnitBalanceSuccess() {
    }

    @Test
    void getKeywordOverviewSuccess() {
        final String phrase = "search";
        final String semrushResponse = "Date;Database;Keyword;Search Volume;CPC;Competition\n" +
                "201903;bo;seo;390;0.44;0.03\n" +
                "201903;hu;seo;1900;0.82;0.45\n" +
                "201903;th;seo;5400;0.96;0.49\n" +
                "201903;cr;seo;590;0.43;0.14";
        final YearMonth date = YearMonth.parse("201903", DateTimeFormatter.ofPattern("yyyyMM"));
        final List<SemrushKeywordOverviewResponse> result = new ArrayList<>();
        result.add(new SemrushKeywordOverviewResponse(date, "bo", "seo", 390, 0.44, 0.03));
        result.add(new SemrushKeywordOverviewResponse(date, "hu", "seo", 1900, 0.82, 0.45));
        result.add(new SemrushKeywordOverviewResponse(date, "th", "seo", 5400, 0.96, 0.49));
        result.add(new SemrushKeywordOverviewResponse(date, "cr", "seo", 590, 0.43, 0.14));

        when(webClientService.retrieve(
                eq(baseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.TEXT_HTML)),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getKeywordOverview(phrase));
    }

    @Test
    void getKDISuccess() {
        final List<String> phrases = List.of("ebay", "seo");
        final String semrushResponse = "Keyword;Keyword Difficulty Index\n" +
                "ebay;95.10\n" +
                "seo;78.35";
        final List<SemrushKDIResponse> result = new ArrayList<>();
        result.add(new SemrushKDIResponse("ebay", 95.10));
        result.add(new SemrushKDIResponse("seo", 78.35));

        when(webClientService.retrieve(
                eq(baseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.TEXT_HTML)),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getKDI(phrases));
    }

    @Test
    void getOrganicCompetitorSuccess() {
        final SemrushOrganicCompetitorRequest request = new SemrushOrganicCompetitorRequest("seobook.com", 0, 10);
        final String semrushResponse = "Domain;Competitor Relevance\n" +
                "seochat.com;0.13\n" +
                "seocentro.com;0.12";
        final List<SemrushOrganicCompetitorResponse> result = new ArrayList<>();
        result.add(new SemrushOrganicCompetitorResponse("seochat.com", 0.13));
        result.add(new SemrushOrganicCompetitorResponse("seocentro.com", 0.12));

        when(webClientService.retrieve(
                eq(baseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.TEXT_HTML)),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getOrganicCompetitor(request));
    }

    @Test
    void getTrafficSummarySuccess() {
        final SemrushTrafficSummaryRequest request = new SemrushTrafficSummaryRequest(List.of("golang.org","blog.golang.org","tour.golang.org/welcome/"),
                YearMonth.of(2023, 2), "CA");
        final String semrushResponse = "target;visits;users\n" +
                "golang.org;4491179;1400453\n" +
                "blog.golang.org;402104;204891\n" +
                "tour.golang.org/welcome/;10131;11628";
        final List<SemrushTrafficSummaryResponse> result = new ArrayList<>();
        result.add(new SemrushTrafficSummaryResponse("golang.org", 4491179, 1400453));
        result.add(new SemrushTrafficSummaryResponse("blog.golang.org", 402104, 204891));
        result.add(new SemrushTrafficSummaryResponse("tour.golang.org/welcome/", 10131, 11628));

        when(webClientService.retrieve(
                eq(apiBaseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.valueOf("text/csv"))),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getTrafficSummary(request));
    }

    @Test
    void getTopPagesSuccess() {
        final SemrushTopPagesRequest request = new SemrushTopPagesRequest("amazon.com", YearMonth.of(2020, 06), "us", 0, 3);
        final String semrushResponse = "page;display_date;traffic_share\n" +
                "amazon.com/s;2020-06-01;1\n" +
                "amazon.com;2020-06-01;0.2545288066748602\n" +
                "amazon.com/gp/css/order-history;2020-06-01;1";
        final List<SemrushTopPagesResponse> result = new ArrayList<>();
        result.add(new SemrushTopPagesResponse("amazon.com/s", LocalDate.of(2020, 06, 01), 1d));
        result.add(new SemrushTopPagesResponse("amazon.com", LocalDate.of(2020, 06, 01), 0.2545288066748602));
        result.add(new SemrushTopPagesResponse("amazon.com/gp/css/order-history", LocalDate.of(2020, 06, 01), 1d));

        when(webClientService.retrieve(
                eq(apiBaseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.valueOf("text/csv"))),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getTopPages(request));
    }
}