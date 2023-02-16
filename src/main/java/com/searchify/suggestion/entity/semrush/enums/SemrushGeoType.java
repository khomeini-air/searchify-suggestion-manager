package com.searchify.suggestion.entity.semrush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SemrushGeoType {
    COUNTRY("country"),
    CONTINENT("continent"),
    SUBCONTINENT("subcontinent");
    private String type;
}
