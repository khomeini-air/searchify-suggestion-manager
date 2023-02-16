package com.searchify.suggestion.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TrafficSummaryResponse {
    private String target;
    private Integer visitAmount;
    private Integer desktopVisits;
    private Integer mobileVisits;
    private Integer pagesPerVisit;
    private Integer desktopPagesPerVisit;
    private Integer mobilePagesPerVisit;
    private Double bounceRate;
    private Double desktopBbounceRate;
    private Double mobileBbounceRate;
    private Integer userAmount;
}