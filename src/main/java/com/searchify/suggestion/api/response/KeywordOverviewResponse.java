package com.searchify.suggestion.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class KeywordOverviewResponse {
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth date;
    private String database;
    private String keyword;
    private Long searchVolume;
    private Double cpc;
    private Double competition;
    private Long results;
    private List<Integer> serpFeatures;
    private List<Double> trends;
    private Double kdi;
    private Integer intent;
}
