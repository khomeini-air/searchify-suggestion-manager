package com.searchify.suggestion.api.controllers;

import com.searchify.suggestion.entity.semrush.request.SemrushTopPagesRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubdomainRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTopSubfolderRequest;
import com.searchify.suggestion.entity.semrush.request.SemrushTrafficSummaryRequest;
import com.searchify.suggestion.entity.semrush.response.SemrushTopPagesResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubdomainResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTopSubfolderResponse;
import com.searchify.suggestion.entity.semrush.response.SemrushTrafficSummaryResponse;
import com.searchify.suggestion.services.SemrushTrafficService;
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
    private SemrushTrafficService semrushService;

    @Test
    void getSummarySuccess() throws Exception {
        final String targets = "golang.org,blog.golang.org,tour.golang.org/welcome/";
        final YearMonth displayDate = YearMonth.of(2023, 2);
        final String country = "CA";
        final String urlString = String.format("/api/analytic/traffic/summary?targets=%s&displayDate=%s&countryCode=%s", targets, "2023-02", country);
        final SemrushTrafficSummaryRequest semrushRequest = new SemrushTrafficSummaryRequest(List.of("golang.org","blog.golang.org","tour.golang.org/welcome/"),
                displayDate, country);
        final List<SemrushTrafficSummaryResponse> semrushResponse = new ArrayList<>();
        semrushResponse.add(new SemrushTrafficSummaryResponse("golang.org", 4491179, 34522,53313,134,958,953,9.2,4.3,1.5,1400453));
        semrushResponse.add(new SemrushTrafficSummaryResponse("blog.golang.org", 402104, 34522,53313,134,958,953,9.2,4.3,1.5,204891));
        semrushResponse.add(new SemrushTrafficSummaryResponse("tour.golang.org/welcome/", 10131, 34522,53313,134,958,953,9.2,4.3,1.5,11628));
        when(semrushService.getTrafficSummary(semrushRequest)).thenReturn(semrushResponse);

        final String resultJson = "[{\"target\":\"golang.org\",\"visitAmount\":4491179,\"desktopVisits\":34522,\"mobileVisits\":53313,\"pagesPerVisit\":134," +
                "\"desktopPagesPerVisit\":958,\"mobilePagesPerVisit\":953,\"bounceRate\":9.2,\"desktopBbounceRate\":4.3,\"mobileBbounceRate\":1.5,\"userAmount\":1400453}," +
                "{\"target\":\"blog.golang.org\",\"visitAmount\":402104,\"desktopVisits\":34522,\"mobileVisits\":53313,\"pagesPerVisit\":134," +
                "\"desktopPagesPerVisit\":958,\"mobilePagesPerVisit\":953,\"bounceRate\":9.2,\"desktopBbounceRate\":4.3,\"mobileBbounceRate\":1.5,\"userAmount\":204891}," +
                "{\"target\":\"tour.golang.org/welcome/\",\"visitAmount\":10131,\"desktopVisits\":34522,\"mobileVisits\":53313,\"pagesPerVisit\":134," +
                "\"desktopPagesPerVisit\":958,\"mobilePagesPerVisit\":953,\"bounceRate\":9.2,\"desktopBbounceRate\":4.3,\"mobileBbounceRate\":1.5,\"userAmount\":11628}]";
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
        final String urlString = String.format("/api/analytic/traffic/top-pages?target=%s&displayDate=%s&countryCode=%s&offset=%s0&limit=%s",
                target, DateTimeFormatter.ofPattern("yyyy-MM").format(displayDate), country, offset, limit);
        final SemrushTopPagesRequest semrushRequest = new SemrushTopPagesRequest("amazon.com", displayDate, country, offset, limit);
        final List<SemrushTopPagesResponse> semrushResponse = new ArrayList<>();
        semrushResponse.add(new SemrushTopPagesResponse("amazon.com/s", LocalDate.of(2020, 06, 01), 1d));
        semrushResponse.add(new SemrushTopPagesResponse("amazon.com", LocalDate.of(2020, 06, 01), 0.2545288066748602));
        semrushResponse.add(new SemrushTopPagesResponse("amazon.com/gp/css/order-history", LocalDate.of(2020, 06, 01), 1d));
        when(semrushService.getTopPages(semrushRequest)).thenReturn(semrushResponse);

        final String resultJson = "[{\"page\":\"amazon.com/s\",\"displayDate\":\"2020-06\",\"trafficShare\":1.0}," +
                "{\"page\":\"amazon.com\",\"displayDate\":\"2020-06\",\"trafficShare\":0.2545288066748602}," +
                "{\"page\":\"amazon.com/gp/css/order-history\",\"displayDate\":\"2020-06\",\"trafficShare\":1.0}]";
        Assert.assertEquals("2020-06-01", formatDisplayDate(displayDate));
        mockMvc.perform(get(urlString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }

    @Test
    void getTopSubfolderSuccess() throws Exception {
        final String target = "amazon.com";
        final YearMonth displayDate = YearMonth.of(2022, 12);
        final Integer offset = 0;
        final Integer limit = 3;
        final String urlString = String.format("/api/analytic/traffic/top-subfolders?target=%s&displayDate=%s&offset=%s&limit=%s",
                target, DateTimeFormatter.ofPattern("yyyy-MM").format(displayDate), offset, limit);
        final SemrushTopSubfolderRequest semrushRequest = new SemrushTopSubfolderRequest("amazon.com", displayDate, offset, limit);
        final List<SemrushTopSubfolderResponse> semrushResponse = new ArrayList<>();
        semrushResponse.add(new SemrushTopSubfolderResponse("/sch/", LocalDate.of(2022, 12, 01), 9.28, 173201982));
        semrushResponse.add(new SemrushTopSubfolderResponse("/mobile/", LocalDate.of(2022, 12, 01), 3.91, 33186275));
        semrushResponse.add(new SemrushTopSubfolderResponse("/mye/", LocalDate.of(2022, 12, 01), 3.19, 76893681));
        when(semrushService.getTopSubfolders(semrushRequest)).thenReturn(semrushResponse);

        final String resultJson = "[{\"page\":\"/sch/\",\"displayDate\":\"2022-12\",\"trafficShare\":9.28,\"uniquePageViews\":173201982}," +
                "{\"page\":\"/mobile/\",\"displayDate\":\"2022-12\",\"trafficShare\":3.91,\"uniquePageViews\":33186275}," +
                "{\"page\":\"/mye/\",\"displayDate\":\"2022-12\",\"trafficShare\":3.19,\"uniquePageViews\":76893681}]";
        Assert.assertEquals("2022-12-01", formatDisplayDate(displayDate));
        mockMvc.perform(get(urlString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }

    @Test
    void getTopSubdomainSuccess() throws Exception {
        final String target = "amazon.com";
        final YearMonth displayDate = YearMonth.of(2022, 12);
        final Integer offset = 0;
        final Integer limit = 3;
        final String urlString = String.format("/api/analytic/traffic/top-subdomains?target=%s&displayDate=%s&offset=%s&limit=%s",
                target, DateTimeFormatter.ofPattern("yyyy-MM").format(displayDate), offset, limit);
        final SemrushTopSubdomainRequest semrushRequest = new SemrushTopSubdomainRequest("amazon.com", displayDate, offset, limit);
        final List<SemrushTopSubdomainResponse> semrushResponse = new ArrayList<>();
        semrushResponse.add(new SemrushTopSubdomainResponse("gaming.amazon.com", LocalDate.of(2022, 12, 01), 24274866, 51.9, 48.1));
        semrushResponse.add(new SemrushTopSubdomainResponse("smile.amazon.com", LocalDate.of(2022, 12, 01), 50300062, 89.25, 10.75));
        semrushResponse.add(new SemrushTopSubdomainResponse("console.aws.amazon.com", LocalDate.of(2022, 12, 01), 14274172, 65.55, 34.45));
        when(semrushService.getTopSubdomains(semrushRequest)).thenReturn(semrushResponse);

        final String resultJson = "[{\"subdomain\":\"gaming.amazon.com\",\"displayDate\":\"2022-12\",\"totalVisits\":24274866,\"desktopShare\":51.9,\"mobileShare\":48.1}," +
                "{\"subdomain\":\"smile.amazon.com\",\"displayDate\":\"2022-12\",\"totalVisits\":50300062,\"desktopShare\":89.25,\"mobileShare\":10.75}," +
                "{\"subdomain\":\"console.aws.amazon.com\",\"displayDate\":\"2022-12\",\"totalVisits\":14274172,\"desktopShare\":65.55,\"mobileShare\":34.45}]";
        Assert.assertEquals("2022-12-01", formatDisplayDate(displayDate));
        mockMvc.perform(get(urlString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(resultJson));
    }

    private String formatDisplayDate(YearMonth date) {
        return String.format("%s-01", DateTimeFormatter.ofPattern("yyyy-MM").format(date));
    }
}
