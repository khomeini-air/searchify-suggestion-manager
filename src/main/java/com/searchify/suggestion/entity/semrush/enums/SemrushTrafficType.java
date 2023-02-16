package com.searchify.suggestion.entity.semrush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SemrushTrafficType {
    ORGANIC("organic"),
    PAID("paid");

    private String type;
}
