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
public class SemrushTrafficSourceResponse extends SemrushBaseResponse {
    @CsvBindByName(column = "from_target")
    private String fromTarget;

    @CsvBindByName(column = "display_date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate displayDate;

    @CsvBindByName(column = "traffic_share")
    private Double trafficShare;

    @CsvBindByName(column = "traffic")
    private Long traffic;

    @CsvBindByName(column = "traffic_type")
    private String trafficType;

    @CsvBindByName(column = "channel")
    private String trafficChannel;

    @CsvBindByName(column = "device_type")
    private String deviceType;
}
