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
public class SemrushTopSubfolderResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "subfolder")
    private String subFolder;

    @CsvBindByName(column = "display_date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate displayDate;
    @CsvBindByName(column = "traffic_share")
    private Double trafficShare;

    @CsvBindByName(column = "unique_pageviews")
    private Integer uniquePageviews;
}
