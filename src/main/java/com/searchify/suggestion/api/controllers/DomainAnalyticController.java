package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.api.response.OrganicCompetitorResponse;
import com.searchify.suggestion.entity.semrush.request.SemrushOrganicCompetitorRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushOrganicCompetitorResponse;
import com.searchify.suggestion.services.SemrushTrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DomainAnalyticController {
    @Autowired
    private SemrushTrafficService semrushService;

    @CrossOrigin
    @GetMapping("/api/analytic/domain/organiccompetitor")
    public ResponseEntity<List<OrganicCompetitorResponse>> getOrganicCompetitor(@RequestParam final String domain,
                                                                                @RequestParam final Integer offset,
                                                                                @RequestParam final Integer limit, 
                                                                                @RequestParam final String database) {
        final List<SemrushOrganicCompetitorResponse> allResult = semrushService.getOrganicCompetitor(new SemrushOrganicCompetitorRequest(domain, offset, limit, database));
        if (allResult.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<OrganicCompetitorResponse> results = allResult.stream().map(sr -> new OrganicCompetitorResponse(sr.getDomain(),
                sr.getCompetitorRelevance())).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(results);
    }
}
