package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatsGetRequestDto;
import ru.practicum.ewm.dto.StatsPostRequestDto;
import ru.practicum.ewm.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void addStats(
            @RequestBody @Valid StatsPostRequestDto statsDto,
            HttpServletRequest request) {
        log.info("{}: сохранение информации о запросе к эндпоинту {}",
                request.getRemoteAddr(), request.getRequestURI());
        statsService.addStats(statsDto);
    }

    @GetMapping("/stats")
    public List<StatsGetRequestDto> getStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение статистики по посещениям",
                request.getRemoteAddr(), request.getRequestURI());
        return statsService.getStats(start, end, uris, unique);
    }
}
