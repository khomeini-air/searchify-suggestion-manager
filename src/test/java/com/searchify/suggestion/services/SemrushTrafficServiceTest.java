package com.searchify.suggestion.services;

import com.searchify.config.test.SearchifyApplicationContextTestConfig;
import com.searchify.suggestion.entity.semrush.enums.SemrushPeriod;
import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficChannel;
import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficType;
import com.searchify.suggestion.entity.semrush.request.SemrushOrganicCompetitorRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubdomainRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubfolderRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSourceRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushOrganicCompetitorResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubdomainResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubfolderResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSourceResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSummaryResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.searchify.suggestion.api.constant.SemrushConstants.COMMA_SEPARATOR;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_ORGANIC;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_SOURCE;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_SUMMARY;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_TOP_PAGES;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_TOP_SUBDOMAINS;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_TOP_SUBFOLDERS;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_ROOT;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_SOURCES;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_SUMMARY;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_TOP_PAGES;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_TOP_SUBDOMAINS;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_TOP_SUBFOLDERS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_COUNTRY;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_DATE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_LIMIT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_OFFSET;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DOMAIN;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_EXPORT_COLUMNS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_KEY;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_SORT_ORDER;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TARGET;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TARGETS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TRAFFIC_CHANNEL;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TRAFFIC_TYPE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TYPE;
import static com.searchify.suggestion.api.constant.SemrushConstants.SORT_ORDER_TRAFFIC_SHARE_DESC;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_DOMAIN_ORGANIC;
import static com.searchify.suggestion.util.SemrushUtil.formatDisplayDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SearchifyApplicationContextTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class SemrushTrafficServiceTest {
    @Autowired
    private SemrushTrafficService semrushService;

    @MockBean
    private WebClientService webClientService;

    @Value("${integration.semrush.api.baseurl}")
    private String apiBaseUrl;

    @Value("${integration.semrush.apikey}")
    private String apiKey;

    @Autowired
    private Environment env;

    @Test
    void getApiUnitBalanceSuccess() {
    }

//    @Test
//    void getOrganicCompetitorSuccess() {
//        final SemrushOrganicCompetitorRequest request = new SemrushOrganicCompetitorRequest("seobook.com", 0, 10, "global");
//        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add(QUERY_PARAM_KEY, apiKey);
//        params.add(QUERY_PARAM_TYPE, TYPE_DOMAIN_ORGANIC);
//        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
//        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
//        params.add(QUERY_PARAM_DOMAIN, request.getDomain());
//        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_ORGANIC);
//        final String semrushResponse = "Domain;Competitor Relevance\n" +
//                "seochat.com;0.13\n" +
//                "seocentro.com;0.12";
//        final List<SemrushOrganicCompetitorResponse> result = new ArrayList<>();
//        result.add(new SemrushOrganicCompetitorResponse("seochat.com", 0.13));
//        result.add(new SemrushOrganicCompetitorResponse("seocentro.com", 0.12));
//
//        when(webClientService.retrieve(
//                apiBaseUrl,
//                PATH_ROOT,
//                params,
//                HttpMethod.GET,
//                Collections.emptyMap(),
//                List.of(MediaType.TEXT_HTML),
//                StringUtils.EMPTY
//        )).thenReturn(semrushResponse);
//
//        assertEquals(result, semrushService.getOrganicCompetitor(request));
//    }

    @Test
    void getTrafficSummarySuccess() {
        final SemrushTrafficSummaryRequest request = new SemrushTrafficSummaryRequest(List.of("golang.org","blog.golang.org","tour.golang.org/welcome/"),
                YearMonth.of(2023, 1), "CA");
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGETS, request.getTargets().stream().collect(Collectors.joining(COMMA_SEPARATOR.toString())));
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_COUNTRY, request.getCountry());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_SUMMARY);
        final String semrushResponse = "display_date;target;visits;desktop_visits;mobile_visits;pages_per_visit;desktop_pages_per_visit;mobile_pages_per_visit;bounce_rate;desktop_bounce_rate;mobile_bounce_rate;users\n" +
                "2023-01-01;golang.org;4491179;1400453;53313;23133;958;953;9.2;4.3;1.5;1400453\n" +
                "2023-01-01;blog.golang.org;402104;204891;23324;11243;892;789;6.3;2.2;0.7;892894\n" +
                "2023-01-01;tour.golang.org/welcome/;10131;11628;11234;14324;112;291;2.9;2.1;0.5;308403";
        final List<SemrushTrafficSummaryResponse> result = new ArrayList<>();
        result.add(new SemrushTrafficSummaryResponse(LocalDate.of(2023, 01, 01), "golang.org", 4491179l, 1400453l,53313l,23133d,958d,953d,9.2,4.3,1.5,1400453l));
        result.add(new SemrushTrafficSummaryResponse(LocalDate.of(2023, 01, 01), "blog.golang.org", 402104l, 204891l,23324l,11243d,892d,789d,6.3,2.2,0.7,892894l));
        result.add(new SemrushTrafficSummaryResponse(LocalDate.of(2023, 01, 01), "tour.golang.org/welcome/", 10131l, 11628l,11234l,14324d,112d,291d,2.9,2.1,0.5,308403l));

        when(webClientService.retrieve(
                apiBaseUrl,
                env.getProperty(PATH_TRAFFIC_SUMMARY),
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        assertEquals(result, semrushService.getTrafficSummary(request));
    }

    @Test
    void getTrafficSummaryHistorySuccess() {
        final String target = "ebay.com";
        final SemrushPeriod period = SemrushPeriod.MONTH;
        final String countryCode = "us";
        final YearMonth yearMonthNow = YearMonth.now();

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGETS, target);
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(yearMonthNow.minusMonths(2)));
        params.add(QUERY_PARAM_COUNTRY, countryCode);
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_SUMMARY);
        final String semrushResponse = "display_date;target;visits;desktop_visits;mobile_visits;pages_per_visit;desktop_pages_per_visit;mobile_pages_per_visit;bounce_rate;desktop_bounce_rate;mobile_bounce_rate;users\n" +
                formatDisplayDate(yearMonthNow.minusMonths(2)) + ";golang.org;4491179;1400453;53313;23133;958;953;9.2;4.3;1.5;1400453";

        when(webClientService.retrieve(
                apiBaseUrl,
                env.getProperty(PATH_TRAFFIC_SUMMARY),
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        final List<SemrushTrafficSummaryResponse> result = List.of(new SemrushTrafficSummaryResponse(LocalDate.of(yearMonthNow.getYear(), yearMonthNow.minusMonths(2).getMonth(), 1),"golang.org", 4491179l, 1400453l,53313l,23133d,958d,953d,9.2,4.3,1.5,1400453l));
        assertEquals(result, semrushService.getTrafficSummaryHistory(target, period, countryCode));
    }

    @Test
    void getTopPagesSuccess() {
        final SemrushTopPagesRequest request = new SemrushTopPagesRequest("amazon.com", YearMonth.of(2020, 06), "us", 0, 3);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_TOP_PAGES);
        final String semrushResponse = "page;display_date;traffic_share\n" +
                "amazon.com/s;2020-06-01;1\n" +
                "amazon.com;2020-06-01;0.2545288066748602\n" +
                "amazon.com/gp/css/order-history;2020-06-01;1";
        final List<SemrushTopPagesResponse> result = new ArrayList<>();
        result.add(new SemrushTopPagesResponse("amazon.com/s", LocalDate.of(2020, 06, 01), 1d));
        result.add(new SemrushTopPagesResponse("amazon.com", LocalDate.of(2020, 06, 01), 0.2545288066748602));
        result.add(new SemrushTopPagesResponse("amazon.com/gp/css/order-history", LocalDate.of(2020, 06, 01), 1d));

        when(webClientService.retrieve(
                apiBaseUrl,
                env.getProperty(PATH_TRAFFIC_TOP_PAGES),
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        assertEquals(result, semrushService.getTopPages(request));
    }

    @Test
    void getTopFolderSuccess() {
        final SemrushTopSubfolderRequest request = new SemrushTopSubfolderRequest("amazon.com",YearMonth.of(2022, 12), 0, 3);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_TOP_SUBFOLDERS);
        final String semrushResponse = "subfolder;display_date;traffic_share;unique_pageviews\n" +
                "/sch/;2022-12-01;9.28;173201982\n" +
                "/mobile/;2022-12-01;3.91;33186275\n" +
                "/mye/;2022-12-01;3.19;76893681";
        final List<SemrushTopSubfolderResponse> result = new ArrayList<>();
        result.add(new SemrushTopSubfolderResponse("/sch/", LocalDate.of(2022, 12, 01), 9.28, 173201982l));
        result.add(new SemrushTopSubfolderResponse("/mobile/", LocalDate.of(2022, 12, 01), 3.91, 33186275l));
        result.add(new SemrushTopSubfolderResponse("/mye/", LocalDate.of(2022, 12, 01), 3.19, 76893681l));

        when(webClientService.retrieve(
                apiBaseUrl,
                env.getProperty(PATH_TRAFFIC_TOP_SUBFOLDERS),
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        assertEquals(result, semrushService.getTopSubfolders(request));
    }

    @Test
    void getTopSubdomainrSuccess() {
        final SemrushTopSubdomainRequest request = new SemrushTopSubdomainRequest("amazon.com",YearMonth.of(2022, 12), 0, 3);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_TOP_SUBDOMAINS);
        final String semrushResponse = "subdomain;display_date;total_visits;desktop_share;mobile_share\n" +
                "gaming.amazon.com;2022-12-01;24274866;51.9;48.1\n" +
                "smile.amazon.com;2022-12-01;50300062;89.25;10.75\n" +
                "console.aws.amazon.com;2022-12-01;14274172;65.55;34.45";
        final List<SemrushTopSubdomainResponse> result = new ArrayList<>();
        result.add(new SemrushTopSubdomainResponse("gaming.amazon.com", LocalDate.of(2022, 12, 01), 24274866l, 51.9, 48.1));
        result.add(new SemrushTopSubdomainResponse("smile.amazon.com", LocalDate.of(2022, 12, 01), 50300062l, 89.25, 10.75));
        result.add(new SemrushTopSubdomainResponse("console.aws.amazon.com", LocalDate.of(2022, 12, 01), 14274172l, 65.55, 34.45));

        when(webClientService.retrieve(
                apiBaseUrl,
                env.getProperty(PATH_TRAFFIC_TOP_SUBDOMAINS),
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        assertEquals(result, semrushService.getTopSubdomains(request));
    }

    @Test
    void getTrafficSourceSuccess() {
        final SemrushTrafficSourceRequest request = new SemrushTrafficSourceRequest("medium.com", YearMonth.of(2022, 12),
                SemrushTrafficType.PAID, SemrushTrafficChannel.DIRECT, 0, 3);
        final LinkedMultiValueMap params = new LinkedMultiValueMap();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_TRAFFIC_TYPE, request.getTrafficType().getType());
        params.add(QUERY_PARAM_TRAFFIC_CHANNEL, request.getTrafficChannel().getChannel());
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_SOURCE);
        params.add(QUERY_PARAM_SORT_ORDER, SORT_ORDER_TRAFFIC_SHARE_DESC);

        final String semrushResponse = "from_target;display_date;traffic_share;traffic;channel;traffic_type\n" +
        "phlap.net;2022-12-01;2.33;7025;referral;paid\n" +
        "blackhatworld.com;2022-12-01;3.4;2342;referral;paid\n" +
        "crunchyroll.com;2022-12-01;5.8;1873;referral;paid";
        final List<SemrushTrafficSourceResponse> result = new ArrayList<>();
        result.add(new SemrushTrafficSourceResponse("phlap.net", LocalDate.of(2022, 12, 01), 2.33d, 7025l, "paid", "referral"));
        result.add(new SemrushTrafficSourceResponse("blackhatworld.com", LocalDate.of(2022, 12, 01), 3.4d, 2342l, "paid", "referral"));
        result.add(new SemrushTrafficSourceResponse("crunchyroll.com", LocalDate.of(2022, 12, 01), 5.8d, 1873l, "paid", "referral"));

        when(webClientService.retrieve(
                apiBaseUrl,
                env.getProperty(PATH_TRAFFIC_SOURCES),
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        assertEquals(result, semrushService.getTrafficSources(request));
    }
}
