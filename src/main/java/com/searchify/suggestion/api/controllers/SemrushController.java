package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.services.SemrushTrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SemrushController {
    @Autowired
    private SemrushTrafficService semrushService;

    @GetMapping("/api/semrush/unitbalance")
    public ResponseEntity<String> getAPIUnitBalance() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(semrushService.getApiUnitBalance());
    }
}
