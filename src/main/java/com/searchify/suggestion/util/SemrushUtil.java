package com.searchify.suggestion.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.searchify.suggestion.entity.semrush.response.SemrushBaseResponse;

import java.io.StringReader;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class SemrushUtil {

    public static List<? extends SemrushBaseResponse> parseCsvResponseBody(final String responseBody,
                                                                     final Character separator,
                                                                     final Class<? extends SemrushBaseResponse> responseClass) {
        if (Objects.isNull(responseBody) || Objects.isNull(separator) || Objects.isNull(responseClass)) {
            return null;
        }

        final StringReader responReader = new StringReader(responseBody);
        final CSVParser csvParser = new CSVParserBuilder().withSeparator(separator).build();
        final CSVReader responseCSVReader = new CSVReaderBuilder(responReader).withCSVParser(csvParser).build();

        return new CsvToBeanBuilder<SemrushBaseResponse>(responseCSVReader)
                .withType(responseClass)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();
    }
    public static String formatDisplayDate(final YearMonth date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return String.format("%s-01", DateTimeFormatter.ofPattern("yyyy-MM").format(date));
    }
}
