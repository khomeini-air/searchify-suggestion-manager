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
public class SemrushTrafficDestinationRequest extends SemrushBaseRequest {
    private String target;
    private YearMonth displayDate;
    private Integer offset;
    private Integer limit;
}
