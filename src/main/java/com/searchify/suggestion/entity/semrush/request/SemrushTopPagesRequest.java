package com.searchify.suggestion.entity.semrush.request;

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
public class SemrushTopPagesRequest extends SemrushBaseRequest {
    private String target;
    private YearMonth displayDate;
    private String country;
    private Integer offset;
    private Integer limit;
}
