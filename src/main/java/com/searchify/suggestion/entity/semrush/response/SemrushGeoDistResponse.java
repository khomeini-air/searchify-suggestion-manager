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
public class SemrushGeoDistResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "geo")
    private String countryCode;

    @CsvBindByName(column = "traffic")
    private Long traffic;

    @CsvBindByName(column = "traffic_share")
    private Double trafficShare;

    @CsvBindByName(column = "desktop_share")
    private Double desktopShare;

    @CsvBindByName(column = "mobile_share")
    private Double mobileShare;

    @CsvBindByName(column = "display_date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate displayDate;
}
