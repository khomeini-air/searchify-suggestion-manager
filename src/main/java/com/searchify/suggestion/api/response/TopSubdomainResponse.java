package com.searchify.suggestion.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TopSubdomainResponse {
    private String subdomain;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth displayDate;

    private Integer totalVisits;
    private Double desktopShare;
    private Double mobileShare;
}
