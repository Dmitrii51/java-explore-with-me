package ru.practicum.stats.service;

import ru.practicum.stats.dto.StatsGetRequestDto;
import ru.practicum.stats.dto.StatsPostRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void addStats(StatsPostRequestDto statsDto);

    List<StatsGetRequestDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique);
}
