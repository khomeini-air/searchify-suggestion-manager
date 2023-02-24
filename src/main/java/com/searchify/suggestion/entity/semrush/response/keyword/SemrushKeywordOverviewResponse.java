package com.searchify.suggestion.entity.semrush.response.keyword;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.searchify.suggestion.entity.semrush.response.SemrushBaseResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.YearMonth;
import java.util.List;

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
    private Long searchVolume;

    @CsvBindByName(column = "CPC")
    private Double cpc;

    @CsvBindByName(column = "Competition")
    private Double competition;

    @CsvBindByName(column = "Number of Results")
    private Long results;

    @CsvBindAndSplitByName(column = "Keywords SERP Features", splitOn = ",", elementType = Integer.class)
    private List<Integer> serpFeatures;

    @CsvBindAndSplitByName(column = "Trends", splitOn = ",", elementType = Double.class)
    private List<Double> trends;

    @CsvBindByName(column = "Keyword Difficulty Index")
    private Double kdi;

    @CsvBindByName(column = "Intent")
    private Integer intent;
}
