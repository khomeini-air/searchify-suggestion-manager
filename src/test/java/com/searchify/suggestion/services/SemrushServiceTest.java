package com.searchify.suggestion.services;

import com.searchify.config.test.SearchifyApplicationContextTestConfig;
import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficChannel;
import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficType;
import com.searchify.suggestion.entity.semrush.request.SemrushOrganicCompetitorRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubdomainRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubfolderRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSourceRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushOrganicCompetitorResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubdomainResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubfolderResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSourceResponse;
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
        final String semrushResponse = "target;visits;desktop_visits;mobile_visits;pages_per_visit;desktop_pages_per_visit;mobile_pages_per_visit;bounce_rate;desktop_bounce_rate;mobile_bounce_rate;users\n" +
                "golang.org;4491179;1400453;53313;23133;958;953;9.2;4.3;1.5;1400453\n" +
                "blog.golang.org;402104;204891;23324;11243;892;789;6.3;2.2;0.7;892894\n" +
                "tour.golang.org/welcome/;10131;11628;11234;14324;112;291;2.9;2.1;0.5;308403";
        final List<SemrushTrafficSummaryResponse> result = new ArrayList<>();
        result.add(new SemrushTrafficSummaryResponse("golang.org", 4491179, 1400453,53313,23133,958,953,9.2,4.3,1.5,1400453));
        result.add(new SemrushTrafficSummaryResponse("blog.golang.org", 402104, 204891,23324,11243,892,789,6.3,2.2,0.7,892894));
        result.add(new SemrushTrafficSummaryResponse("tour.golang.org/welcome/", 10131, 11628,11234,14324,112,291,2.9,2.1,0.5,308403));

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

    @Test
    void getTopFolderSuccess() {
        final SemrushTopSubfolderRequest request = new SemrushTopSubfolderRequest("amazon.com",YearMonth.of(2022, 12), 3);
        final String semrushResponse = "subfolder;display_date;traffic_share;unique_pageviews\n" +
                "/sch/;2022-12-01;9.28;173201982\n" +
                "/mobile/;2022-12-01;3.91;33186275\n" +
                "/mye/;2022-12-01;3.19;76893681";
        final List<SemrushTopSubfolderResponse> result = new ArrayList<>();
        result.add(new SemrushTopSubfolderResponse("/sch/", LocalDate.of(2022, 12, 01), 9.28, 173201982));
        result.add(new SemrushTopSubfolderResponse("/mobile/", LocalDate.of(2022, 12, 01), 3.91, 33186275));
        result.add(new SemrushTopSubfolderResponse("/mye/", LocalDate.of(2022, 12, 01), 3.19, 76893681));

        when(webClientService.retrieve(
                eq(apiBaseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.valueOf("text/csv"))),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getTopSubfolders(request));
    }

    @Test
    void getTopSubdomainrSuccess() {
        final SemrushTopSubdomainRequest request = new SemrushTopSubdomainRequest("amazon.com",YearMonth.of(2022, 12), 0, 3);
        final String semrushResponse = "subdomain;display_date;total_visits;desktop_share;mobile_share\n" +
                "gaming.amazon.com;2022-12-01;24274866;51.9;48.1\n" +
                "smile.amazon.com;2022-12-01;50300062;89.25;10.75\n" +
                "console.aws.amazon.com;2022-12-01;14274172;65.55;34.45";
        final List<SemrushTopSubdomainResponse> result = new ArrayList<>();
        result.add(new SemrushTopSubdomainResponse("gaming.amazon.com", LocalDate.of(2022, 12, 01), 24274866, 51.9, 48.1));
        result.add(new SemrushTopSubdomainResponse("smile.amazon.com", LocalDate.of(2022, 12, 01), 50300062, 89.25, 10.75));
        result.add(new SemrushTopSubdomainResponse("console.aws.amazon.com", LocalDate.of(2022, 12, 01), 14274172, 65.55, 34.45));

        when(webClientService.retrieve(
                eq(apiBaseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.valueOf("text/csv"))),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getTopSubdomains(request));
    }

    @Test
    void getTrafficSourceSuccess() {
        final SemrushTrafficSourceRequest request = new SemrushTrafficSourceRequest("medium.com", YearMonth.of(2022, 12),
                SemrushTrafficType.PAID, SemrushTrafficChannel.DIRECT, 0, 3);
        final String semrushResponse = "from_target;display_date;traffic;channel;traffic_type\n" +
        "phlap.net;2022-12-01;7025;referral;paid\n" +
        "blackhatworld.com;2022-12-01;2342;referral;paid\n" +
        "crunchyroll.com;2022-12-01;1873;referral;organic";
        final List<SemrushTrafficSourceResponse> result = new ArrayList<>();
        result.add(new SemrushTrafficSourceResponse("phlap.net", LocalDate.of(2022, 12, 01), 7025, "paid", "referral"));
        result.add(new SemrushTrafficSourceResponse("blackhatworld.com", LocalDate.of(2022, 12, 01), 2342, "paid", "referral"));
        result.add(new SemrushTrafficSourceResponse("crunchyroll.com", LocalDate.of(2022, 12, 01), 1873, "organic", "referral"));

        when(webClientService.retrieve(
                eq(apiBaseUrl),
                any(Function.class),
                eq(HttpMethod.GET),
                eq(Collections.emptyMap()),
                eq(List.of(MediaType.valueOf("text/csv"))),
                eq(StringUtils.EMPTY)
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getTrafficSources(request));
    }
}