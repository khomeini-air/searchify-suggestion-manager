package com.searchify.suggestion.entity.semrush.request;

import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficChannel;
import com.searchify.suggestion.entity.semrush.enums.SemrushTrafficType;
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
public class SemrushTrafficSourceRequest extends SemrushBaseRequest {
    private String target;
    private YearMonth displayDate;
    private SemrushTrafficType trafficType;
    private SemrushTrafficChannel trafficChannel;
    private Integer offset;
    private Integer limit;
}
