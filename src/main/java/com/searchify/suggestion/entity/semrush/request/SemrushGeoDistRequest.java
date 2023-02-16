package com.searchify.suggestion.entity.semrush.request;

import com.searchify.suggestion.entity.semrush.enums.SemrushGeoType;
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
public class SemrushGeoDistRequest extends SemrushBaseRequest {
    private String target;
    private SemrushGeoType geoType;
    private YearMonth displayDate;
    private Integer offset;
    private Integer limit;
}
