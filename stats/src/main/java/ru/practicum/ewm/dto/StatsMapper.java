package ru.practicum.ewm.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.model.Stats;

@UtilityClass
public class StatsMapper {

    public Stats fromStatsPostRequestDto(StatsPostRequestDto statsDto) {
        return new Stats(
                statsDto.getId(),
                statsDto.getApp(),
                statsDto.getUri(),
                statsDto.getIp(),
                statsDto.getTimestamp(),
                0
        );
    }

    public StatsGetRequestDto toStatsGetRequestDto(Stats stats) {
        return new StatsGetRequestDto(
                stats.getApp(),
                stats.getUri(),
                stats.getHits()
        );
    }
}
