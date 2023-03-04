package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.entity.semrush.request.SemrushOrganicCompetitorRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushOrganicCompetitorResponse;
import com.searchify.suggestion.services.SemrushTrafficService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DomainAnalyticController.class)
class DomainAnalyticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SemrushTrafficService semrushService;

//    @Test
//    void getOrganicCompetitorSuccess() throws Exception {
//        final String domain = "seobook.com";
//        final Integer offset = 0;
//        final Integer limit = 10;
//        final String urlString = String.format("/api/analytic/domain/organiccompetitor?offset=%s&limit=%s&domain=%s", offset, limit, domain);
//        final List<SemrushOrganicCompetitorResponse> semrushResult = new ArrayList<>();
//        semrushResult.add(new SemrushOrganicCompetitorResponse("seochat.com", 1.99));
//        semrushResult.add(new SemrushOrganicCompetitorResponse("seocentro.com", 8.24));
//        when(semrushService.getOrganicCompetitor(new SemrushOrganicCompetitorRequest(domain, offset, limit, "global"))).thenReturn(semrushResult);
//
//        final String resultJson = "[{\"domain\":\"seochat.com\",\"competitorRelevance\":1.99},{\"domain\":\"seocentro.com\",\"competitorRelevance\":8.24}]";
//        mockMvc.perform(get(urlString))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().string(resultJson));
//    }
}