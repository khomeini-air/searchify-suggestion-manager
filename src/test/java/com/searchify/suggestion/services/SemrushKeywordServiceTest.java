package com.searchify.suggestion.services;

import com.searchify.config.test.SearchifyApplicationContextTestConfig;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordBroadMatchRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordQuestionRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordRelatedRequest;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordBroadMatchResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordQuestionResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordRelatedResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
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

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.searchify.suggestion.api.constant.SemrushConstants.CSV_SEPARATOR;
import static com.searchify.suggestion.api.constant.SemrushConstants.DATABASE_DEFAULT;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_KDI;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_KEYWORD_OVERVIEW;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_PHRASE_FULLSEARCH;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_PHRASE_QUESTIONS;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_PHRASE_RELATED;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_ROOT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DATABASE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_LIMIT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_OFFSET;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_SORT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_EXPORT_COLUMNS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_KEY;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_PHRASE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TYPE;
import static com.searchify.suggestion.api.constant.SemrushConstants.SORT_ORDER_VOLUME;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_FULLSEARCH;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_KDI;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_QUESTIONS;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_RELATED;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_THIS;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SearchifyApplicationContextTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class SemrushKeywordServiceTest {
    @Autowired
    private SemrushKeywordService semrushService;

    @MockBean
    private WebClientService webClientService;

    @Value("${integration.semrush.api.baseurl}")
    private String apiBaseUrl;

    @Value("${integration.semrush.apikey}")
    private String apiKey;

    @Autowired
    private Environment env;

    /*@Test
    void getKeywordOverviewSuccess() {
        final String phrase = "search";
        final String database = "us";
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_THIS);
        params.add(QUERY_PARAM_PHRASE, phrase);
        params.add(QUERY_PARAM_DATABASE, database);
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KEYWORD_OVERVIEW);
        final String semrushResponse = "Date;Database;Keyword;Search Volume;CPC;Competition;Number of Results;Trends;Keyword Difficulty Index;Keywords SERP Features;Intent\n" +
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
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_HTML),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getKeywordOverview(phrase, database));
    }*/

    @Test
    void getKeywordOverviewSuccess() {
        final String phrase = "search";
        final String database = "us";
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_THIS);
        params.add(QUERY_PARAM_PHRASE, phrase);
        params.add(QUERY_PARAM_DATABASE, database);
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KEYWORD_OVERVIEW);
        final String semrushResponse = "Date;Database;Keyword;Search Volume;CPC;Competition;Number of Results;Trends;Keyword Difficulty Index;Keywords SERP Features;Intent\n" +
                "201903;cr;seo;590;0.43;0.14;12190000000;0.66,0.66,0.66,0.66,0.81,0.66,0.66,0.81,1.00,0.66,0.66,0.54;100;6,7,13,21;1";
        final YearMonth date = YearMonth.parse("201903", DateTimeFormatter.ofPattern("yyyyMM"));
        final List<SemrushKeywordOverviewResponse> result = new ArrayList<>();
        result.add(new SemrushKeywordOverviewResponse(date, "cr", "seo", 590l, 0.43, 0.14,12190000000L, List.of(6,7,13,21), List.of(0.66,0.66,0.66,0.66,0.81,0.66,0.66,0.81,1.00,0.66,0.66,0.54), 100d, 1));

        when(webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getKeywordOverview(phrase, database));
    }

    @Test
    void getKeywordOverviewNoDateResponseSuccess() {
        final String phrase = "search";
        final String database = "us";
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_THIS);
        params.add(QUERY_PARAM_PHRASE, phrase);
        params.add(QUERY_PARAM_DATABASE, database);
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KEYWORD_OVERVIEW);
        final String semrushResponse = "Database;Keyword;Search Volume;CPC;Competition;Number of Results;Trends;Keyword Difficulty Index;Keywords SERP Features;Intent\n" +
                "cr;seo;590;0.43;0.14;12190000000;0.66,0.66,0.66,0.66,0.81,0.66,0.66,0.81,1.00,0.66,0.66,0.54;100;6,7,13,21;1";
        final YearMonth date = null;
        final List<SemrushKeywordOverviewResponse> result = new ArrayList<>();
        result.add(new SemrushKeywordOverviewResponse(date, "cr", "seo", 590l, 0.43, 0.14,12190000000L, List.of(6,7,13,21), List.of(0.66,0.66,0.66,0.66,0.81,0.66,0.66,0.81,1.00,0.66,0.66,0.54), 100d, 1));

        when(webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getKeywordOverview(phrase, database));
    }

    @Test
    void getKDISuccess() {
        final List<String> phrases = List.of("ebay", "seo");
        final String database = "us";
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_KDI);
        params.add(QUERY_PARAM_PHRASE, phrases.stream().collect(Collectors.joining(CSV_SEPARATOR.toString())));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KDI);
        params.add(QUERY_PARAM_DATABASE, StringUtils.isEmpty(database) ? DATABASE_DEFAULT : database);
        final String semrushResponse = "Keyword;Keyword Difficulty Index\n" +
                "ebay;95.10\n" +
                "seo;78.35";
        final List<SemrushKDIResponse> result = new ArrayList<>();
        result.add(new SemrushKDIResponse("ebay", 95.10));
        result.add(new SemrushKDIResponse("seo", 78.35));

        when(webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getKDI(phrases, database));
    }

    @Test
    void getKeywordBroadMatchesSuccess() {
        final String phrase = "nike";
        final String database = "us";
        final Integer offset = 0;
        final Integer limit = 3;
        final SemrushKeywordBroadMatchRequest request = new SemrushKeywordBroadMatchRequest(phrase, database, offset, limit);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_FULLSEARCH);
        params.add(QUERY_PARAM_PHRASE, request.getPhrase());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_PHRASE_FULLSEARCH);
        params.add(QUERY_PARAM_DATABASE, request.getDatabase());
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_DISPLAY_SORT, SORT_ORDER_VOLUME);
        final String semrushResponse = "Keyword;Search Volume;Keyword Difficulty Index\n" +
                "nike;5000000;90";
        final List<SemrushKeywordBroadMatchResponse> result = new ArrayList<>();
        result.add(new SemrushKeywordBroadMatchResponse(phrase, 5000000l, 90d));

        when(webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getBroadMatch(request));
    }

    @Test
    void getKeywordQuestionsSuccess() {
        final String phrase = "nike";
        final String database = "us";
        final Integer offset = 0;
        final Integer limit = 3;
        final SemrushKeywordQuestionRequest request = new SemrushKeywordQuestionRequest(phrase, database, offset, limit);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_QUESTIONS);
        params.add(QUERY_PARAM_PHRASE, request.getPhrase());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_PHRASE_QUESTIONS);
        params.add(QUERY_PARAM_DATABASE, request.getDatabase());
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_DISPLAY_SORT, SORT_ORDER_VOLUME);

        final String semrushResponse = "Keyword;Search Volume;Keyword Difficulty Index\n" +
                "nike;5000000;90";
        final List<SemrushKeywordQuestionResponse> result = new ArrayList<>();
        result.add(new SemrushKeywordQuestionResponse(phrase, 5000000l, 90d));

        when(webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getQuestion(request));
    }

    @Test
    void getKeywordRelatedSuccess() {
        final String phrase = "nike";
        final String database = "us";
        final Integer offset = 0;
        final Integer limit = 3;
        final SemrushKeywordRelatedRequest request = new SemrushKeywordRelatedRequest(phrase, database, offset, limit);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_RELATED);
        params.add(QUERY_PARAM_PHRASE, request.getPhrase());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_PHRASE_RELATED);
        params.add(QUERY_PARAM_DATABASE, request.getDatabase());
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_DISPLAY_SORT, SORT_ORDER_VOLUME);
        final String semrushResponse = "Keyword;Search Volume;Keyword Difficulty Index\n" +
                "nike;5000000;90";
        final List<SemrushKeywordRelatedResponse> result = new ArrayList<>();
        result.add(new SemrushKeywordRelatedResponse(phrase, 5000000l, 90d));

        when(webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY
        )).thenReturn(semrushResponse);

        Assertions.assertEquals(result, semrushService.getRelated(request));
    }
}
