package com.searchify.suggestion.entity.semrush.response;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SemrushAgeSexDistResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "age")
    private String age;

    @CsvBindByName(column = "female_users")
    private Integer femaleUsers;

    @CsvBindByName(column = "male_users")
    private Integer maleUsers;

    @CsvBindByName(column = "female_share")
    private Double femaleShare;

    @CsvBindByName(column = "male_share")
    private Double maleShare;

    @CsvBindByName(column = "display_date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate displayDate;
}
