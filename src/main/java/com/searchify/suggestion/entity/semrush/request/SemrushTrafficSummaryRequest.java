package com.searchify.suggestion.entity.semrush.request;

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
public class SemrushTrafficSummaryRequest extends SemrushBaseRequest{
    private List<String> targets;
    private YearMonth displayDate;
    private String country;
}
