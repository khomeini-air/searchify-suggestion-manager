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
public class SemrushTopSubdomainResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "subdomain")
    private String subdomain;

    @CsvBindByName(column = "display_date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate displayDate;

    @CsvBindByName(column = "total_visits")
    private Long totalVisits;

    @CsvBindByName(column = "desktop_share")
    private Double desktopShare;

    @CsvBindByName(column = "mobile_share")
    private Double mobileShare;
}
