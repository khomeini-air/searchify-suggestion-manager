package com.searchify.suggestion.services;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.searchify.suggestion.entity.semrush.enums.SemrushPeriod;
import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficChannel;
import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficType;
import com.searchify.suggestion.entity.semrush.request.SemrushAgeSexDistRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushGeoDistRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushOrganicCompetitorRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubdomainRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubfolderRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficDestinationRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSourceRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushAgeSexDistResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushBaseResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushGeoDistResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushOrganicCompetitorResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubdomainResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubfolderResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficDestinationResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSourceResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSummaryResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.StringReader;
import java.net.URI;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.searchify.suggestion.api.constant.SemrushConstants.COMMA_SEPARATOR;
import static com.searchify.suggestion.api.constant.SemrushConstants.CSV_SEPARATOR;
import static com.searchify.suggestion.api.constant.SemrushConstants.DATABASE_DEFAULT;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_AGESEX_DISTRIBUTION;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_GEO_DISTRIBUTION;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_KDI;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_ORGANIC;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_DESTINATION;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_SOURCE;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_SUMMARY;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_TOP_SUBDOMAINS;
import static com.searchify.suggestion.api.constant.SemrushConstants.EXPORT_COLUMNS_TRAFFIC_TOP_SUBFOLDERS;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_COUNT_API_UNIT;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_ROOT;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_AGE_SEX_DIST;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_DESTINATIONS;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_GEO_DIST;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_SOURCES;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_SUMMARY;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_TOP_PAGES;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_TOP_SUBDOMAINS;
import static com.searchify.suggestion.api.constant.SemrushConstants.PATH_TRAFFIC_TOP_SUBFOLDERS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_COUNTRY;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DATABASE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_DATE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_LIMIT;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DISPLAY_OFFSET;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_DOMAIN;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_EXPORT_COLUMNS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_KEY;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_PHRASE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_SORT_ORDER;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TARGET;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TARGETS;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TRAFFIC_CHANNEL;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TRAFFIC_TYPE;
import static com.searchify.suggestion.api.constant.SemrushConstants.QUERY_PARAM_TYPE;
import static com.searchify.suggestion.api.constant.SemrushConstants.SORT_ORDER_GEO_DISTRIBUTION;
import static com.searchify.suggestion.api.constant.SemrushConstants.SORT_ORDER_TRAFFIC_DESTINATION;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_DOMAIN_ORGANIC;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_ALL;
import static com.searchify.suggestion.api.constant.SemrushConstants.TYPE_PHRASE_KDI;

@Service
public class SemrushService {

    @Value("${integration.semrush.baseurl}")
    private String baseUrl;

    @Value("${integration.semrush.api.baseurl}")
    private String apiBaseUrl;

    @Value("${integration.semrush.apikey}")
    private String apiKey;

    @Autowired
    private Environment env;

    @Autowired
    private WebClientService webClientService;

    public String getApiUnitBalance() {
        return webClientService.retrieve(
                baseUrl,
                uriBuilder -> uriBuilder.path(env.getProperty(PATH_COUNT_API_UNIT)).queryParam(QUERY_PARAM_KEY, apiKey).build(),
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_HTML),
                StringUtils.EMPTY
        );
    }

    public List<SemrushKeywordOverviewResponse> getKeywordOverview(@NotEmpty final String phrase) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_ALL);
        params.add(QUERY_PARAM_PHRASE, phrase);
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(PATH_ROOT, params);
        final String responseBody = webClientService.retrieve(
                baseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_HTML),
                StringUtils.EMPTY);

        return (List<SemrushKeywordOverviewResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKeywordOverviewResponse.class);
    }

    public List<SemrushKDIResponse> getKDI(@NotEmpty final List<String> phrases) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_PHRASE_KDI);
        params.add(QUERY_PARAM_PHRASE, phrases.stream().collect(Collectors.joining(CSV_SEPARATOR.toString())));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_KDI);
        params.add(QUERY_PARAM_DATABASE, DATABASE_DEFAULT);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(PATH_ROOT, params);

        final String responseBody = webClientService.retrieve(
                baseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_HTML),
                StringUtils.EMPTY);

        return (List<SemrushKDIResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushKDIResponse.class);
    }

    public List<SemrushOrganicCompetitorResponse> getOrganicCompetitor(@NotNull final SemrushOrganicCompetitorRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TYPE, TYPE_DOMAIN_ORGANIC);
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_DOMAIN, request.getDomain());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_ORGANIC);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(PATH_ROOT, params);

        final String responseBody = webClientService.retrieve(
                baseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.TEXT_HTML),
                StringUtils.EMPTY);

        return (List<SemrushOrganicCompetitorResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushOrganicCompetitorResponse.class);
    }

    public List<SemrushTrafficSummaryResponse> getTrafficSummary(@NotNull final SemrushTrafficSummaryRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGETS, request.getTargets().stream().collect(Collectors.joining(COMMA_SEPARATOR.toString())));
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_COUNTRY, request.getCountry());
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_SUMMARY);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_SUMMARY), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushTrafficSummaryResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushTrafficSummaryResponse.class);
    }

    public List<SemrushTrafficSummaryResponse> getTrafficSummaryHistory(final String target, final SemrushPeriod period) {
        final List<SemrushTrafficSummaryResponse> result = new ArrayList<>();
        for (int i = 1; i <= period.getLength(); i++) {
            final SemrushTrafficSummaryRequest request = new SemrushTrafficSummaryRequest(List.of(target), YearMonth.now().minusMonths(i), null);
            result.addAll(getTrafficSummary(request));
        }

        return result;
    }

    public List<SemrushTopPagesResponse> getTopPages(@NotNull final SemrushTopPagesRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_SUMMARY);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_TOP_PAGES), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushTopPagesResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushTopPagesResponse.class);

    }

    public List<SemrushTopSubfolderResponse> getTopSubfolders(final SemrushTopSubfolderRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_TOP_SUBFOLDERS);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_TOP_SUBFOLDERS), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushTopSubfolderResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushTopSubfolderResponse.class);
    }

    public List<SemrushTopSubdomainResponse> getTopSubdomains(final SemrushTopSubdomainRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_TOP_SUBDOMAINS);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_TOP_SUBDOMAINS), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushTopSubdomainResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushTopSubdomainResponse.class);
    }

    public List<SemrushTrafficSourceResponse> getTrafficSources(final SemrushTrafficSourceRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_TRAFFIC_TYPE, request.getTrafficType().getType());
        params.add(QUERY_PARAM_TRAFFIC_CHANNEL, request.getTrafficChannel().getChannel());
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_SOURCE);
        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_SOURCES), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushTrafficSourceResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushTrafficSourceResponse.class);
    }

    public Map<String, List<SemrushTrafficSourceResponse>> getTrafficSourceHistoryByType(final String target, final SemrushTrafficType type, final SemrushPeriod period) {
        final Map<String, List<SemrushTrafficSourceResponse>> results = new HashMap<>();

        Stream.of(SemrushTrafficChannel.values()).forEach(v -> {
                    final List<SemrushTrafficSourceResponse> responses = new ArrayList<>();
                    for (int i = 1; i <= period.getLength(); i++) {
                        final SemrushTrafficSourceRequest request = new SemrushTrafficSourceRequest(target, YearMonth.now().minusMonths(i), type, v, 0, 1);
                        responses.addAll(getTrafficSources(request));
                    }
                    results.put(v.getChannel(), responses);
                }
        );

        return results;
    }

    public List<SemrushTrafficDestinationResponse> getTrafficDestinations(final SemrushTrafficDestinationRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_TRAFFIC_DESTINATION);
        params.add(QUERY_PARAM_SORT_ORDER, SORT_ORDER_TRAFFIC_DESTINATION);

        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_DESTINATIONS), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushTrafficDestinationResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushTrafficDestinationResponse.class);
    }

    public List<SemrushAgeSexDistResponse> getAgeSexDistribution(final SemrushAgeSexDistRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS, EXPORT_COLUMNS_AGESEX_DISTRIBUTION);

        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_AGE_SEX_DIST), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushAgeSexDistResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushAgeSexDistResponse.class);
    }

    public List<SemrushGeoDistResponse> getGeoDistribution(final SemrushGeoDistRequest request) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(QUERY_PARAM_KEY, apiKey);
        params.add(QUERY_PARAM_TARGET, request.getTarget());
        params.add(QUERY_PARAM_DISPLAY_DATE, formatDisplayDate(request.getDisplayDate()));
        params.add(QUERY_PARAM_DISPLAY_OFFSET, String.valueOf(request.getOffset()));
        params.add(QUERY_PARAM_DISPLAY_LIMIT, String.valueOf(request.getLimit()));
        params.add(QUERY_PARAM_EXPORT_COLUMNS,  EXPORT_COLUMNS_GEO_DISTRIBUTION);
        params.add(QUERY_PARAM_SORT_ORDER, SORT_ORDER_GEO_DISTRIBUTION);

        final Function<UriBuilder, URI> uriFunction = composeURIFunction(env.getProperty(PATH_TRAFFIC_GEO_DIST), params);

        final String responseBody = webClientService.retrieve(
                apiBaseUrl,
                uriFunction,
                HttpMethod.GET,
                Collections.emptyMap(),
                List.of(MediaType.valueOf("text/csv")),
                StringUtils.EMPTY);

        return (List<SemrushGeoDistResponse>) parseCsvResponseBody(responseBody, CSV_SEPARATOR, SemrushGeoDistResponse.class);
    }

    private Function<UriBuilder, URI> composeURIFunction(final String path, final MultiValueMap<String, String> params) {
        return uriBuilder -> uriBuilder.path(path).queryParams(params).build();
    }

    private List<? extends SemrushBaseResponse> parseCsvResponseBody(final String responseBody,
                                                                     final Character separator,
                                                                     final Class<? extends SemrushBaseResponse> responseClass) {
        final StringReader responReader = new StringReader(responseBody);
        final CSVParser csvParser = new CSVParserBuilder().withSeparator(separator).build();
        final CSVReader responseCSVReader = new CSVReaderBuilder(responReader).withCSVParser(csvParser).build();

        return new CsvToBeanBuilder<SemrushBaseResponse>(responseCSVReader)
                .withType(responseClass)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();
    }

    private String formatDisplayDate(final YearMonth date) {
        return String.format("%s-01", DateTimeFormatter.ofPattern("yyyy-MM").format(date));
    }
}
