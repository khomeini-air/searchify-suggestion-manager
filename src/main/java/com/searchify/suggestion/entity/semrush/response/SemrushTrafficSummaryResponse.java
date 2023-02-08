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
    private Integer visitAmount;

    @CsvBindByName(column = "users")
    private Integer userAmount;
}
