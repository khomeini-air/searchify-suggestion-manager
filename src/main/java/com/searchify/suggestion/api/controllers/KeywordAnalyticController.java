package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.api.response.KDIResponse;
import com.searchify.suggestion.api.response.KeywordOverviewResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.services.SemrushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class KeywordAnalyticController {
    @Autowired
    private SemrushService semrushService;

    @GetMapping("/api/analytic/keyword/overview")
    public ResponseEntity<KeywordOverviewResponse> getKeywordOverview(@NotEmpty @RequestParam final String phrase) {
        final List<SemrushKeywordOverviewResponse> allResult = semrushService.getKeywordOverview(phrase);
        if (allResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final Integer searchVolume = allResult.stream().mapToInt(response -> response.getSearchVolume()).sum();
        final Double cpc = allResult.stream().mapToDouble(response -> response.getCpc()).average().orElse(Double.NaN);
        final YearMonth date = allResult.stream().findFirst().map(SemrushKeywordOverviewResponse::getDate).orElse(null);
        final String keyword = allResult.stream().findFirst().map(SemrushKeywordOverviewResponse::getKeyword).orElse(null);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new KeywordOverviewResponse(date, keyword, searchVolume, cpc));
    }

    @GetMapping("/api/analytic/keyword/kdi")
    public ResponseEntity<List<KDIResponse>> getKDI(@RequestParam final String phrases) {
        final List<SemrushKDIResponse> semrushResponse = semrushService.getKDI(Arrays.asList(phrases.split(";")));
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<KDIResponse> result = semrushResponse.stream().map(e -> new KDIResponse(e.getKeyword(), e.getKdi())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}
