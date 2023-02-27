package com.searchify.suggestion.entity.semrush.request;

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
public class SemrushKeywordQuestionRequest extends SemrushBaseRequest {
    private String phrase;
    private String database;
    private Integer offset;
    private Integer limit;
}
