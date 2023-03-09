package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.api.response.KDIResponse;
import com.searchify.suggestion.api.response.KeywordBroadMatchResponse;
import com.searchify.suggestion.api.response.KeywordOverviewResponse;
import com.searchify.suggestion.api.response.KeywordQuestionResponse;
import com.searchify.suggestion.api.response.KeywordRelatedResponse;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordBroadMatchRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordQuestionRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushKeywordRelatedRequest;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordBroadMatchResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordQuestionResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordRelatedResponse;
import com.searchify.suggestion.services.SemrushKeywordService;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
public class KeywordAnalyticController {
    @Autowired
    private SemrushKeywordService semrushService;

    @CrossOrigin
    @GetMapping("/api/analytic/keyword/overview")
    public ResponseEntity<List<KeywordOverviewResponse>> getKeywordOverview(@RequestParam final String phrase,
                                                                            @RequestParam(required = false) final String database,
                                                                            @RequestParam
                                                                            @DateTimeFormat(pattern = "yyyy-MM")
                                                                            @Schema(pattern = "yyyy-MM", example = "2023-01") final YearMonth displayDate) {
        final List<SemrushKeywordOverviewResponse> semrushResult = semrushService.getKeywordOverview(phrase, database, displayDate);
        if (semrushResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<KeywordOverviewResponse> responses = semrushResult.stream().map(e -> new KeywordOverviewResponse(e.getDate(),
                e.getDatabase(), e.getKeyword(), e.getSearchVolume(), e.getCpc(), e.getCompetition(), e.getResults(),
                e.getSerpFeatures(), e.getTrends(), e.getKdi(), e.getIntent())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responses);
    }
    
    @CrossOrigin
    @GetMapping("/api/analytic/keyword/kdi")
    public ResponseEntity<List<KDIResponse>> getKDI(@RequestParam final String phrases,
                                                    @RequestParam final String database) {
        final List<SemrushKDIResponse> semrushResponse = semrushService.getKDI(Arrays.asList(phrases.split(";")), database);
        if (semrushResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<KDIResponse> result = semrushResponse.stream().map(e -> new KDIResponse(e.getKeyword(), e.getKdi())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
    @CrossOrigin
    @GetMapping("/api/analytic/keyword/broad-matches")
    public ResponseEntity<List<KeywordBroadMatchResponse>> getKeywordBroadMatches(@RequestParam final String phrase,
                                                                                  @RequestParam final String database,
                                                                                  @RequestParam @Min(0) @Max(10000)
                                                                                  @Schema(description = "Skip the specified number of result") final Integer offset,
                                                                                  @RequestParam @Min(1) @Max(5000)
                                                                                  @Schema(description = "The number of results returned") final Integer limit) {
        final List<SemrushKeywordBroadMatchResponse> semrushResult = semrushService.getBroadMatch(new SemrushKeywordBroadMatchRequest(phrase, database, offset, limit));
        if (semrushResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<KeywordBroadMatchResponse> responses = semrushResult.stream().map(e -> new KeywordBroadMatchResponse(e.getKeyword(),
                e.getSearchVolume(), e.getKdi())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responses);
    }
    
    @CrossOrigin
    @GetMapping("/api/analytic/keyword/questions")
    public ResponseEntity<List<KeywordQuestionResponse>> getKeywordQuestions(@RequestParam final String phrase,
                                                                             @RequestParam final String database,
                                                                             @RequestParam @Min(0) @Max(10000)
                                                                             @Schema(description = "Skip the specified number of result") final Integer offset,
                                                                             @RequestParam @Min(1) @Max(5000)
                                                                             @Schema(description = "The number of results returned") final Integer limit) {
        final List<SemrushKeywordQuestionResponse> semrushResult = semrushService.getQuestion(new SemrushKeywordQuestionRequest(phrase, database, offset, limit));
        if (semrushResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<KeywordQuestionResponse> responses = semrushResult.stream().map(e -> new KeywordQuestionResponse(e.getKeyword(),
                e.getSearchVolume(), e.getKdi())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responses);
    }
    
    @CrossOrigin
    @GetMapping("/api/analytic/keyword/related")
    public ResponseEntity<List<KeywordRelatedResponse>> getKeywordRelated(@RequestParam final String phrase,
                                                                          @RequestParam final String database,
                                                                          @RequestParam @Min(0) @Max(10000)
                                                                          @Schema(description = "Skip the specified number of result") final Integer offset,
                                                                          @RequestParam @Min(1) @Max(5000)
                                                                          @Schema(description = "The number of results returned") final Integer limit) {
        final List<SemrushKeywordRelatedResponse> semrushResult = semrushService.getRelated(new SemrushKeywordRelatedRequest(phrase, database, offset, limit));
        if (semrushResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<KeywordRelatedResponse> responses = semrushResult.stream().map(e -> new KeywordRelatedResponse(e.getKeyword(),
                e.getSearchVolume(), e.getKdi())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responses);
    }
}
