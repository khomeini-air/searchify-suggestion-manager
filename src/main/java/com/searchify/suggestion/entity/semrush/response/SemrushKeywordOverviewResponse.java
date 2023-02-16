package com.searchify.suggestion.entity.semrush.response;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SemrushKeywordOverviewResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "Date")
    @CsvDate(value = "yyyyMM")
    private YearMonth date;

    @CsvBindByName(column = "Database")
    private String database;

    @CsvBindByName(column = "Keyword")
    private String keyword;

    @CsvBindByName(column = "Search Volume")
    private Integer searchVolume;

    @CsvBindByName(column = "CPC")
    private Double cpc;

    @CsvBindByName(column = "Competition")
    private Double competition;
}
