package com.searchify.suggestion.entity.semrush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SemrushPeriod {
    MONTH(1),
    SEMESTER(6),
    YEAR(12);

    private int length;
}
