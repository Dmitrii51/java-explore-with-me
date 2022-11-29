package ru.practicum.ewm.dto;

import ru.practicum.ewm.model.Stats;

public class StatsMapper {

    public static Stats fromStatsPostRequestDto(StatsPostRequestDto statsDto) {
        return new Stats(
                statsDto.getId(),
                statsDto.getApp(),
                statsDto.getUri(),
                statsDto.getIp(),
                statsDto.getTimestamp(),
                0
        );
    }

    public static StatsGetRequestDto toStatsGetRequestDto(Stats stats) {
        return new StatsGetRequestDto(
                stats.getApp(),
                stats.getUri(),
                stats.getHits()
        );
    }
}
