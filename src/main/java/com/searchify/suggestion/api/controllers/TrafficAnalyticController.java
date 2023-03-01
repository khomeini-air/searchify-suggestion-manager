package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.api.response.TopPagesResponse;
import com.searchify.suggestion.api.response.TopSubdomainResponse;
import com.searchify.suggestion.api.response.TopSubfolderResponse;
import com.searchify.suggestion.api.response.TrafficAgeSexDistResponse;
import com.searchify.suggestion.api.response.TrafficDestinationResponse;
import com.searchify.suggestion.api.response.TrafficGeoDistResponse;
import com.searchify.suggestion.api.response.TrafficSummaryResponse;
import com.searchify.suggestion.entity.semrush.enums.SemrushGeoType;
import com.searchify.suggestion.entity.semrush.enums.SemrushPeriod;
import com.searchify.suggestion.entity.semrush.request.SemrushAgeSexDistRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushGeoDistRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubdomainRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubfolderRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficDestinationRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushAgeSexDistResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushGeoDistResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubdomainResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubfolderResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficDestinationResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSummaryResponse;
import com.searchify.suggestion.services.SemrushTrafficService;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TrafficAnalyticController {
    @Autowired
    private SemrushTrafficService semrushService;

    @GetMapping("/api/analytic/traffic/summary")
    public ResponseEntity<List<TrafficSummaryResponse>> getSummary(@RequestParam
                                                                   @Schema(description = "all target websites separated by comma",
                                                                           example = "amazon.com,ebay.com") final String targets,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM")
                                                                   @Schema(pattern = "yyyy-MM", example = "2023-02") final YearMonth displayDate,
                                                                   @RequestParam(required = false) @Schema(example = "us") final String countryCode) {
        final SemrushTrafficSummaryRequest semrushRequest = new SemrushTrafficSummaryRequest(Arrays.asList(targets.split(",")), displayDate, countryCode);
        final List<SemrushTrafficSummaryResponse> semrushResponse = semrushService.getTrafficSummary(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TrafficSummaryResponse> result = semrushResponse.stream().map(e -> new TrafficSummaryResponse(e.getTarget(),
                e.getVisits(), e.getDesktopVisits(), e.getMobileVisits(), e.getPagesPerVisit(), e.getDesktopPagesPerVisit(),
                e.getMobilePagesPerVisit(), e.getBounceRate(), e.getDesktopBbounceRate(), e.getMobileBbounceRate(), e.getUserAmount(), YearMonth.from(e.getDisplayDate()))).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }


    @GetMapping("/api/analytic/traffic/summary/history")
    public ResponseEntity<List<TrafficSummaryResponse>> getSummaryHistory(@RequestParam
                                                                          @Schema(description = "a target website",
                                                                                  example = "ebay.com") final String target,
                                                                          @RequestParam
                                                                          @Schema(description = "history period with only 3 valid values: " +
                                                                                  "MONTH, SEMESTER, YEAR") final SemrushPeriod period,
                                                                          @RequestParam(required = false) @Schema(example = "us") final String countryCode) {
        final List<SemrushTrafficSummaryResponse> semrushResponse = semrushService.getTrafficSummaryHistory(target, period, countryCode);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        final List<TrafficSummaryResponse> result = semrushResponse.stream().map(e -> new TrafficSummaryResponse(e.getTarget(),
                e.getVisits(), e.getDesktopVisits(), e.getMobileVisits(), e.getPagesPerVisit(), e.getDesktopPagesPerVisit(),
                e.getMobilePagesPerVisit(), e.getBounceRate(), e.getDesktopBbounceRate(), e.getMobileBbounceRate(), e.getUserAmount(), YearMonth.from(e.getDisplayDate()))).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/api/analytic/traffic/top-pages")
    public ResponseEntity<List<TopPagesResponse>> getTopPages(@RequestParam
                                                              @Schema(description = "target website",
                                                                      example = "ebay.com") final String target,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM")
                                                              @Schema(pattern = "yyyy-MM", example = "2023-02") final YearMonth displayDate,
                                                              @RequestParam(required = false) @Schema(example = "us") final String countryCode,
                                                              @RequestParam @Min(0) @Max(10000)
                                                              @Schema(description = "Skip the specified number of result") final Integer offset,
                                                              @RequestParam @Min(1) @Max(5000)
                                                              @Schema(description = "The number of results returned") final Integer limit) {
        final SemrushTopPagesRequest semrushRequest = new SemrushTopPagesRequest(target, displayDate, countryCode, offset, limit);
        final List<SemrushTopPagesResponse> semrushResponse = semrushService.getTopPages(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TopPagesResponse> result = semrushResponse.stream().map(e -> new TopPagesResponse(e.getPage(),
                YearMonth.from(e.getDisplayDate()), e.getTrafficShare())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/api/analytic/traffic/top-subfolders")
    public ResponseEntity<List<TopSubfolderResponse>> getTopSubfolders(@RequestParam
                                                                       @Schema(description = "target website",
                                                                               example = "ebay.com") final String target,
                                                                       @RequestParam
                                                                       @DateTimeFormat(pattern = "yyyy-MM")
                                                                       @Schema(pattern = "yyyy-MM", example = "2023-02") final YearMonth displayDate,
                                                                       @RequestParam @Min(0) @Max(10000)
                                                                       @Schema(description = "Skip the specified number of result") final Integer offset,
                                                                       @RequestParam @Min(1) @Max(5000)
                                                                       @Schema(description = "The number of results returned") final Integer limit) {
        final SemrushTopSubfolderRequest semrushRequest = new SemrushTopSubfolderRequest(target, displayDate, offset, limit);
        final List<SemrushTopSubfolderResponse> semrushResponse = semrushService.getTopSubfolders(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TopSubfolderResponse> result = semrushResponse.stream().map(e -> new TopSubfolderResponse(e.getSubFolder(),
                YearMonth.from(e.getDisplayDate()), e.getTrafficShare(), e.getUniquePageviews())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/api/analytic/traffic/top-subdomains")
    public ResponseEntity<List<TopSubdomainResponse>> getTopSubdomains(@RequestParam
                                                                       @Schema(description = "target website",
                                                                               example = "ebay.com") final String target,
                                                                       @RequestParam
                                                                       @DateTimeFormat(pattern = "yyyy-MM")
                                                                       @Schema(pattern = "yyyy-MM", example = "2023-02") final YearMonth displayDate,
                                                                       @RequestParam @Min(0) @Max(10000)
                                                                       @Schema(description = "Skip the specified number of result") final Integer offset,
                                                                       @RequestParam @Min(1) @Max(5000)
                                                                       @Schema(description = "The number of results returned") final Integer limit) {
        final SemrushTopSubdomainRequest semrushRequest = new SemrushTopSubdomainRequest(target, displayDate, offset, limit);
        final List<SemrushTopSubdomainResponse> semrushResponse = semrushService.getTopSubdomains(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TopSubdomainResponse> result = semrushResponse.stream().map(e -> new TopSubdomainResponse(e.getSubdomain(),
                YearMonth.from(e.getDisplayDate()), e.getTotalVisits(), e.getDesktopShare(), e.getMobileShare())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/api/analytic/traffic/destinations")
    public ResponseEntity<List<TrafficDestinationResponse>> getTrafficDestination(@RequestParam
                                                                                  @Schema(description = "target website",
                                                                                          example = "ebay.com") final String target,
                                                                                  @RequestParam
                                                                                  @DateTimeFormat(pattern = "yyyy-MM")
                                                                                  @Schema(pattern = "yyyy-MM", example = "2023-02") final YearMonth displayDate,
                                                                                  @RequestParam @Min(0) @Max(10000)
                                                                                  @Schema(description = "Skip the specified number of result") final Integer offset,
                                                                                  @RequestParam @Min(1) @Max(5000)
                                                                                  @Schema(description = "The number of results returned") final Integer limit) {
        final SemrushTrafficDestinationRequest semrushRequest = new SemrushTrafficDestinationRequest(target, displayDate, offset, limit);
        final List<SemrushTrafficDestinationResponse> semrushResponse = semrushService.getTrafficDestinations(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TrafficDestinationResponse> result = semrushResponse.stream().map(e -> new TrafficDestinationResponse(e.getToTarget(), e.getTrafficShare(),
                YearMonth.from(e.getDisplayDate()))).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/api/analytic/traffic/age-sex-distribution")
    public ResponseEntity<List<TrafficAgeSexDistResponse>> getAgeSexDistribution(@RequestParam
                                                                                 @Schema(description = "target website",
                                                                                         example = "ebay.com") final String target,
                                                                                 @RequestParam
                                                                                 @DateTimeFormat(pattern = "yyyy-MM")
                                                                                 @Schema(pattern = "yyyy-MM", example = "2023-02") final YearMonth displayDate) {
        final SemrushAgeSexDistRequest semrushRequest = new SemrushAgeSexDistRequest(target, displayDate);
        final List<SemrushAgeSexDistResponse> semrushResponse = semrushService.getAgeSexDistribution(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TrafficAgeSexDistResponse> result = semrushResponse.stream().map(e -> new TrafficAgeSexDistResponse(e.getAge(), e.getFemaleUsers(),
                e.getMaleUsers(), e.getFemaleShare(), e.getMaleShare(), YearMonth.from(e.getDisplayDate()))).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/api/analytic/traffic/geo-distribution")
    public ResponseEntity<List<TrafficGeoDistResponse>> getGeoDistribution(@RequestParam
                                                                           @Schema(description = "target website",
                                                                                   example = "ebay.com") final String target,
                                                                           @RequestParam
                                                                           @DateTimeFormat(pattern = "yyyy-MM")
                                                                           @Schema(pattern = "yyyy-MM", example = "2023-02") final YearMonth displayDate,
                                                                           @RequestParam
                                                                           @Schema(description = "Geo type") final SemrushGeoType geoType,
                                                                           @RequestParam @Min(0) @Max(10000)
                                                                           @Schema(description = "Skip the specified number of result") final Integer offset,
                                                                           @RequestParam @Min(1) @Max(5000)
                                                                           @Schema(description = "The number of results returned") final Integer limit) {
        final SemrushGeoDistRequest semrushRequest = new SemrushGeoDistRequest(target, geoType, displayDate, offset, limit);
        final List<SemrushGeoDistResponse> semrushResponse = semrushService.getGeoDistribution(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TrafficGeoDistResponse> result = semrushResponse.stream().map(e -> new TrafficGeoDistResponse(e.getCountryCode(), e.getTraffic(),
                e.getTrafficShare(), e.getDesktopShare(), e.getMobileShare(), YearMonth.from(e.getDisplayDate()))).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}
