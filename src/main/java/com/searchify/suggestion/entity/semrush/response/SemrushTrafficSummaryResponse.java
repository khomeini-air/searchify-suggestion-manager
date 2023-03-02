package com.searchify.suggestion.entity.semrush.response;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SemrushTrafficSummaryResponse extends SemrushBaseResponse{
    @CsvBindByName(column = "display_date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate displayDate;

    @CsvBindByName(column = "target")
    private String target;

    @CsvBindByName(column = "visits")
    private Long visits;

    @CsvBindByName(column = "desktop_visits")
    private Long desktopVisits;

    @CsvBindByName(column = "mobile_visits")
    private Long mobileVisits;

    @CsvBindByName(column = "pages_per_visit")
    private Double pagesPerVisit;

    @CsvBindByName(column = "desktop_pages_per_visit")
    private Double desktopPagesPerVisit;

    @CsvBindByName(column = "mobile_pages_per_visit")
    private Double mobilePagesPerVisit;

    @CsvBindByName(column = "bounce_rate")
    private Double bounceRate;

    @CsvBindByName(column = "desktop_bounce_rate")
    private Double desktopBbounceRate;

    @CsvBindByName(column = "mobile_bounce_rate")
    private Double mobileBbounceRate;

    @CsvBindByName(column = "users")
    private Long userAmount;
}
