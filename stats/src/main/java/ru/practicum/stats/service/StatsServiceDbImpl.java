package ru.practicum.stats.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.StatsGetRequestDto;
import ru.practicum.stats.dto.StatsMapper;
import ru.practicum.stats.dto.StatsPostRequestDto;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class StatsServiceDbImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceDbImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public void addStats(StatsPostRequestDto statsDto) {
        statsDto.setId(null);
        Stats newStats =  statsRepository.save(StatsMapper.fromStatsPostRequestDto(statsDto));
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
