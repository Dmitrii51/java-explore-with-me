package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsGetRequestDto {
    private String app;
    private String uri;
    private long hits;
}
