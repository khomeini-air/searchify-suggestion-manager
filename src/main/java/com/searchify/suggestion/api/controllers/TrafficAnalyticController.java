package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.api.response.TopPagesResponse;
import com.searchify.suggestion.api.response.TrafficSummaryResponse;
import com.searchify.suggestion.entity.semrush.enums.SemrushPeriodEnum;
import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSummaryResponse;
import com.searchify.suggestion.services.SemrushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TrafficAnalyticController {
    @Autowired
    private SemrushService semrushService;

    @GetMapping("/api/analytic/traffic/summary")
    public ResponseEntity<List<TrafficSummaryResponse>> getSummary(@RequestParam final String targets,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM") final YearMonth displayDate,
                                                                   @RequestParam(required = false) final String country) {
        final SemrushTrafficSummaryRequest semrushRequest = new SemrushTrafficSummaryRequest(Arrays.asList(targets.split(",")), displayDate, country);
        final List<SemrushTrafficSummaryResponse> semrushResponse = semrushService.getTrafficSummary(semrushRequest);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TrafficSummaryResponse> result = semrushResponse.stream().map(e -> new TrafficSummaryResponse(e.getTarget(),
                e.getVisitAmount(), e.getUserAmount())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }


    @GetMapping("/api/analytic/traffic/summary/history")
    public ResponseEntity<List<TrafficSummaryResponse>> getSummaryHistory(@RequestParam final String target,
                                                                   @RequestParam final SemrushPeriodEnum period) {
        final List<SemrushTrafficSummaryResponse> semrushResponse = semrushService.getTrafficSummaryHistory(target, period);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<TrafficSummaryResponse> result = semrushResponse.stream().map(e -> new TrafficSummaryResponse(e.getTarget(),
                e.getVisitAmount(), e.getUserAmount())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/api/analytic/traffic/top-pages")
    public ResponseEntity<List<TopPagesResponse>> getTopPages(@RequestParam final String target,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM") final YearMonth displayDate,
                                                              @RequestParam(required = false) final String country,
                                                              @RequestParam final Integer offset,
                                                              @RequestParam final Integer limit) {
        final SemrushTopPagesRequest semrushRequest = new SemrushTopPagesRequest(target, displayDate, country, offset, limit);
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
}
