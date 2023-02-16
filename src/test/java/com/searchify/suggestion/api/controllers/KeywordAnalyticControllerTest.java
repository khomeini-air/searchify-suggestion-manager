package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.entity.semrush.response.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.services.SemrushService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KeywordAnalyticController.class)
class KeywordAnalyticControllerTest {
    private final static YearMonth DATE = YearMonth.parse("201903", DateTimeFormatter.ofPattern("yyyyMM"));
    private final static String PHRASE = "seo";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SemrushService semrushService;

    @Test
    void getKeywordOverviewSuccess() throws Exception {
        final List<SemrushKeywordOverviewResponse> semrushResult = new ArrayList<>();

        semrushResult.add(new SemrushKeywordOverviewResponse(DATE, "bo","seo",390, 0.44, 0.03));
        semrushResult.add(new SemrushKeywordOverviewResponse(DATE, "hu","seo",1900,0.82,0.45));
        semrushResult.add(new SemrushKeywordOverviewResponse(DATE, "th","seo",5400,0.96,0.49));
        semrushResult.add(new SemrushKeywordOverviewResponse(DATE, "cr","seo",590,0.43, 0.14));

        final String resultJson = "{\"date\":\"2019-03\",\"keyword\":\"seo\",\"searchVolume\":8280,\"cpc\":0.6625}";
        when(semrushService.getKeywordOverview(PHRASE)).thenReturn(semrushResult);

        mockMvc.perform(get("/api/analytic/keyword/overview?phrase=seo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }

    @Test
    void getKeywordOverviewEmptyResult() throws Exception {
        when(semrushService.getKeywordOverview(PHRASE)).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(get("/api/analytic/keyword/overview?phrase=seo"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(StringUtils.EMPTY));
    }

    @Test
    void getKDIsuccess() throws Exception {
        final List<SemrushKDIResponse> semrushResult = List.of(new SemrushKDIResponse("seo", 95.10),
                new SemrushKDIResponse("ebay", 78.35));

        final String resultJson = "[{\"keyword\":\"seo\",\"kdi\":95.1},{\"keyword\":\"ebay\",\"kdi\":78.35}]";
        when(semrushService.getKDI(List.of("seo","ebay"))).thenReturn(semrushResult);

        mockMvc.perform(get("/api/analytic/keyword/kdi?phrases=seo;ebay"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }
}