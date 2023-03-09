package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKDIResponse;
import com.searchify.suggestion.entity.semrush.response.keyword.SemrushKeywordOverviewResponse;
import com.searchify.suggestion.services.SemrushKeywordService;
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
    private final static String DATABASE = "us";
    private final static YearMonth DISPLAY_DATE = YearMonth.of(2023, 1);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SemrushKeywordService semrushKeywordService;

    @Test
    void getKeywordOverviewSuccess() throws Exception {
        final List<SemrushKeywordOverviewResponse> semrushResult = new ArrayList<>();
        semrushResult.add(new SemrushKeywordOverviewResponse(DATE, "cr", "seo", 590l, 0.43, 0.14,12190000000L, List.of(6,7,13,21), List.of(0.66,0.66,0.66,0.66,0.81,0.66,0.66,0.81,1.00,0.66,0.66,0.54), 100d, 1));

        final String resultJson = "[{\"date\":\"2019-03\",\"database\":\"cr\",\"keyword\":\"seo\",\"searchVolume\":590,\"cpc\":0.43," +
                "\"competition\":0.14,\"results\":12190000000,\"serpFeatures\":[6,7,13,21],\"trends\":[0.66,0.66,0.66,0.66,0.81,0.66,0.66,0.81,1.0,0.66,0.66,0.54]," +
                "\"kdi\":100.0,\"intent\":1}]";
        when(semrushKeywordService.getKeywordOverview(PHRASE, DATABASE, DISPLAY_DATE)).thenReturn(semrushResult);

        mockMvc.perform(get("/api/analytic/keyword/overview?phrase=seo&database=us&displayDate=2023-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }

    @Test
    void getKeywordOverviewEmptyResult() throws Exception {
        when(semrushKeywordService.getKeywordOverview(PHRASE, DATABASE, DISPLAY_DATE)).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(get("/api/analytic/keyword/overview?phrase=seo&database=us&displayDate=2023-01"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(StringUtils.EMPTY));
    }

    @Test
    void getKDIsuccess() throws Exception {
        final List<SemrushKDIResponse> semrushResult = List.of(new SemrushKDIResponse("seo", 95.10),
                new SemrushKDIResponse("ebay", 78.35));

        final String resultJson = "[{\"keyword\":\"seo\",\"kdi\":95.1},{\"keyword\":\"ebay\",\"kdi\":78.35}]";
        when(semrushKeywordService.getKDI(List.of("seo","ebay"), "us")).thenReturn(semrushResult);

        mockMvc.perform(get("/api/analytic/keyword/kdi?phrases=seo;ebay&database=us"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }
}