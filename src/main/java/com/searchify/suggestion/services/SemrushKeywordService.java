package com.searchify.suggestion.services;

import com.searchify.suggestion.entity.semrush.request.SemrushKeywordBroadMatchRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordQuestionRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordRelatedRequest;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordBroadMatchResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordQuestionResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordRelatedResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.searchify.suggestion.api.constant.SemrushConstants.CSV_SEPARATOR;
import static com.searchify.suggestion.api.constant.SemrushConstants.DATABASE_DEFAULT;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_KDI;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_KEYWORD_ALL;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_KEYWORD_OVERVIEW;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_PHRASE_FULLSEARCH;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_PHRASE_QUESTIONS;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_PHRASE_RELATED;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_ROOT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DATABASE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_DATE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_LIMIT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_OFFSET;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_SORT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_EXPORT_COLUMNS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_KEY;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_PHRASE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TYPE;
import static com.searchify.suggestion.api.constant.SemrushConstants.SORT_ORDER_VOLUME;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_ALL;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_FULLSEARCH;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_KDI;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_QUESTIONS;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_RELATED;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_THIS;
import static com.searchify.suggestion.util.SemrushUtil.formatDateYyyyMM15;
import static com.searchify.suggestion.util.SemrushUtil.parseCsvResponseBody;

@Service
@Validated
public class SemrushKeywordService {
    @Value("${integration.semrush.api.baseurl}")
    private String apiBaseUrl;

    @Value("${integration.semrush.apikey}")
    private String apiKey;

    @Autowired
    private Environment env;

    @Autowired
    private WebClientService webClientService;

    public List<SemrushKeywordOverviewResponse> getKeywordOverview(@NotEmpty final String phrase,
                                                                   @NotEmpty final String database,
                                                                   @NotEmpty final YearMonth displayDate) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_THIS);
        params.add(QUERY_PARAM_PHRASE, phrase);
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDateYyyyMM15(displayDate));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KEYWORD_OVERVIEW);

        if (StringUtils.isNotEmpty(database)) {
            params.add(QUERY_PARAM_DATABASE, database);
        }

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY);

        return (List<SemrushKeywordOverviewResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKeywordOverviewResponse.class);
    }

    public List<SemrushKeywordOverviewResponse> getKeywordOverviewAll(@NotEmpty final String phrase) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_ALL);
        params.add(QUERY_PARAM_PHRASE, phrase);
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KEYWORD_ALL);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY);

        return (List<SemrushKeywordOverviewResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKeywordOverviewResponse.class);
    }

    public List<SemrushKDIResponse> getKDI(@NotEmpty final List<String> phrases, @NotEmpty final String database) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_KDI);
        params.add(QUERY_PARAM_PHRASE, phrases.stream().collect(Collectors.joining(CSV_SEPARATOR.toString())));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KDI);
        params.add(QUERY_PARAM_DATABASE, StringUtils.isEmpty(database) ? DATABASE_DEFAULT : database);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY);

        return (List<SemrushKDIResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKDIResponse.class);
    }

    public List<SemrushKeywordBroadMatchResponse> getBroadMatch(@NotNull final SemrushKeywordBroadMatchRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_FULLSEARCH);
        params.add(QUERY_PARAM_PHRASE, request.getPhrase());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_PHRASE_FULLSEARCH);
        params.add(QUERY_PARAM_DATABASE, StringUtils.isEmpty(request.getDatabase()) ? DATABASE_DEFAULT : request.getDatabase());
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_DISPLAY_SORT, SORT_ORDER_VOLUME);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY);

        return (List<SemrushKeywordBroadMatchResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKeywordBroadMatchResponse.class);
    }

    public List<SemrushKeywordQuestionResponse> getQuestion(@NotNull final SemrushKeywordQuestionRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_QUESTIONS);
        params.add(QUERY_PARAM_PHRASE, request.getPhrase());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_PHRASE_QUESTIONS);
        params.add(QUERY_PARAM_DATABASE, StringUtils.isEmpty(request.getDatabase()) ? DATABASE_DEFAULT : request.getDatabase());
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_DISPLAY_SORT, SORT_ORDER_VOLUME);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY);

        return (List<SemrushKeywordQuestionResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKeywordQuestionResponse.class);
    }

    public List<SemrushKeywordRelatedResponse> getRelated(@NotNull final SemrushKeywordRelatedRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_RELATED);
        params.add(QUERY_PARAM_PHRASE, request.getPhrase());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_PHRASE_RELATED);
        params.add(QUERY_PARAM_DATABASE, StringUtils.isEmpty(request.getDatabase()) ? DATABASE_DEFAULT : request.getDatabase());
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_DISPLAY_SORT, SORT_ORDER_VOLUME);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                PATH_ROOT,
                params,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_PLAIN),
                StringUtils.EMPTY);

        return (List<SemrushKeywordRelatedResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKeywordRelatedResponse.class);
    }


}
