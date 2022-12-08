package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.StatsGetRequestDto;
import ru.practicum.ewm.dto.StatsPostRequestDto;

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
