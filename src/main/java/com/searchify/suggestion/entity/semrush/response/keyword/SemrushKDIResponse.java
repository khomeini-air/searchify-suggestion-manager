package com.searchify.suggestion.entity.semrush.response.keyword;

import com.opencsv.bean.CsvBindByName;
import com.searchify.suggestion.entity.semrush.response.SemrushBaseResponse;
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
public class SemrushKDIResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "Keyword")
    private String keyword;

    @CsvBindByName(column = "Keyword Difficulty Index")
    private Double kdi;
}
