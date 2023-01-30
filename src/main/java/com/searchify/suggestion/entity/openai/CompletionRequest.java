package com.searchify.suggestion.entity.openai;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CompletionRequest {
    @NotEmpty
    private String writtenType;

    @NotEmpty
    private String domain;

    @NotNull
    @Min(1)
    private Integer maxTokens;
}
