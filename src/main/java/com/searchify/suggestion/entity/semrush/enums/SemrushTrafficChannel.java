package com.searchify.suggestion.entity.semrush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SemrushTrafficChannel {
    DIRECT("direct"),
    REFERRAL("referral"),
    SEARCH("search"),
    SOCIAL("social");

    private String channel;
}
