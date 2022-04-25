package com.searchify.suggestion.entity;

import java.util.Objects;

public class SuggestionResultDto {

    private final Suggestion suggestion;

    public SuggestionResultDto(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuggestionResultDto that = (SuggestionResultDto) o;
        return Objects.equals(suggestion, that.suggestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suggestion);
    }
}