package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.StatsGetRequestDto;
import ru.practicum.ewm.dto.StatsMapper;
import ru.practicum.ewm.dto.StatsPostRequestDto;
import ru.practicum.ewm.model.Stats;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceDbImpl implements StatsService {

    private final StatsRepository statsRepository;

    public void addStats(StatsPostRequestDto statsDto) {
        statsDto.setId(null);
        Stats newStats = statsRepository.save(StatsMapper.fromStatsPostRequestDto(statsDto));
        log.info("Добавление информации в БД - {}", newStats);
    }

    public List<StatsGetRequestDto> getStats(
            LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatsGetRequestDto> stats;
        if (start == null) {
            start = LocalDateTime.now().minusDays(100);
        }

        if (end == null) {
            end = LocalDateTime.now();
        }

        if (uris == null) {
            if (unique) {
                stats = statsRepository.getUniqueStats(start, end);
            } else {
                stats = statsRepository.getStats(start, end);
            }
        } else {
            if (unique) {
                stats = statsRepository.getUniqueStats(start, end, uris);
            } else {
                stats = statsRepository.getStats(start, end, uris);
            }
        }
        return stats;
    }
}
