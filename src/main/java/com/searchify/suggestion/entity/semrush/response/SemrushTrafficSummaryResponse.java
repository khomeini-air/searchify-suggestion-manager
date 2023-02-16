package com.searchify.suggestion.entity.semrush.response;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SemrushTrafficSummaryResponse extends SemrushBaseResponse{
    @CsvBindByName(column = "target")
    private String target;

    @CsvBindByName(column = "visits")
    private Integer visits;

    @CsvBindByName(column = "desktop_visits")
    private Integer desktopVisits;

    @CsvBindByName(column = "mobile_visits")
    private Integer mobileVisits;

    @CsvBindByName(column = "pages_per_visit")
    private Integer pagesPerVisit;

    @CsvBindByName(column = "desktop_pages_per_visit")
    private Integer desktopPagesPerVisit;

    @CsvBindByName(column = "mobile_pages_per_visit")
    private Integer mobilePagesPerVisit;

    @CsvBindByName(column = "bounce_rate")
    private Double bounceRate;

    @CsvBindByName(column = "desktop_bounce_rate")
    private Double desktopBbounceRate;

    @CsvBindByName(column = "mobile_bounce_rate")
    private Double mobileBbounceRate;

    @CsvBindByName(column = "users")
    private Integer userAmount;
}
