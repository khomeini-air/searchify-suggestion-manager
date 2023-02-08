package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSummaryResponse;
import com.searchify.suggestion.services.SemrushService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrafficAnalyticController.class)
class TrafficAnalyticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SemrushService semrushService;

    @Test
    void getSummarySuccess() throws Exception {
        final String targets = "golang.org,blog.golang.org,tour.golang.org/welcome/";
        final YearMonth displayDate = YearMonth.of(2023, 2);
        final String country = "CA";
        final String urlString = String.format("/api/analytic/traffic/summary?targets=%s&displayDate=%s&country=%s", targets, "2023-02", country);
        final SemrushTrafficSummaryRequest semrushRequest = new SemrushTrafficSummaryRequest(List.of("golang.org","blog.golang.org","tour.golang.org/welcome/"),
                displayDate, country);
        final List<SemrushTrafficSummaryResponse> semrushResponse = new ArrayList<>();
        semrushResponse.add(new SemrushTrafficSummaryResponse("golang.org", 4491179, 1400453));
        semrushResponse.add(new SemrushTrafficSummaryResponse("blog.golang.org", 402104, 204891));
        semrushResponse.add(new SemrushTrafficSummaryResponse("tour.golang.org/welcome/", 10131, 11628));
        when(semrushService.getTrafficSummary(semrushRequest)).thenReturn(semrushResponse);

        final String resultJson = "[{\"target\":\"golang.org\",\"visitAmount\":4491179,\"userAmount\":1400453},{\"target\":\"blog.golang.org\",\"visitAmount\":402104,\"userAmount\":204891},{\"target\":\"tour.golang.org/welcome/\",\"visitAmount\":10131,\"userAmount\":11628}]";
        Assert.assertEquals("2023-02-01", formatDisplayDate(displayDate));
        mockMvc.perform(get(urlString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }

    @Test
    void getTopPagesSuccess() throws Exception {
        final String target = "amazon.com";
        final YearMonth displayDate = YearMonth.of(2020, 06);
        final String country = "us";
        final Integer offset = 0;
        final Integer limit = 3;
        final String urlString = String.format("/api/analytic/traffic/top-pages?target=%s&displayDate=%s&country=%s&offset=%s0&limit=%s",
                target, DateTimeFormatter.ofPattern("yyyy-MM").format(displayDate), country, offset, limit);
        final SemrushTopPagesRequest semrushRequest = new SemrushTopPagesRequest("amazon.com", displayDate, country, offset, limit);
        final List<SemrushTopPagesResponse> semrushResponse = new ArrayList<>();
        semrushResponse.add(new SemrushTopPagesResponse("amazon.com/s", LocalDate.of(2020, 06, 01), 1d));
        semrushResponse.add(new SemrushTopPagesResponse("amazon.com", LocalDate.of(2020, 06, 01), 0.2545288066748602));
        semrushResponse.add(new SemrushTopPagesResponse("amazon.com/gp/css/order-history", LocalDate.of(2020, 06, 01), 1d));
        when(semrushService.getTopPages(semrushRequest)).thenReturn(semrushResponse);

        final String resultJson = "[{\"page\":\"amazon.com/s\",\"displayDate\":\"2020-06\",\"trafficShare\":1.0},{\"page\":\"amazon.com\",\"displayDate\":\"2020-06\",\"trafficShare\":0.2545288066748602},{\"page\":\"amazon.com/gp/css/order-history\",\"displayDate\":\"2020-06\",\"trafficShare\":1.0}]";
        Assert.assertEquals("2020-06-01", formatDisplayDate(displayDate));
        mockMvc.perform(get(urlString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }

    private String formatDisplayDate(YearMonth date) {
        return String.format("%s-01", DateTimeFormatter.ofPattern("yyyy-MM").format(date));
    }
}