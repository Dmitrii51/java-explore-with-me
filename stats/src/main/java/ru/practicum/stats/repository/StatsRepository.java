package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.dto.StatsGetRequestDto;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("SELECT new ru.practicum.stats.dto.StatsGetRequestDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 AND " +
            "s.uri IN ?3 " +
            "GROUP BY s.uri, s.app")
    List<StatsGetRequestDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.dto.model.StatsGetRequestDto(s.app, s.uri, COUNT(DISTINCT s.ip))  " +
            "FROM Stats as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 AND " +
            "s.uri IN ?3 " +
            "GROUP BY s.uri, s.app")
    List<StatsGetRequestDto> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stats.dto.StatsGetRequestDto(s.app, s.uri, COUNT(s.ip))  " +
            "FROM Stats as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.uri, s.app")
    List<StatsGetRequestDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.stats.dto.StatsGetRequestDto(s.app, s.uri, COUNT(DISTINCT s.ip))  " +
            "FROM Stats as s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.uri, s.app")
    List<StatsGetRequestDto> getUniqueStats(LocalDateTime start, LocalDateTime end);
}
