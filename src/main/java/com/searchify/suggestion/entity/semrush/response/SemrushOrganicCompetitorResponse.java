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
public class SemrushOrganicCompetitorResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "Domain")
    private String domain;

    @CsvBindByName(column = "Competitor Relevance")
    private Double competitorRelevance;
}
