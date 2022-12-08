package ru.practicum.ewm.compilation.controller.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение подборок событий",
                request.getRemoteAddr(), request.getRequestURI());
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    CompilationDto getCompilationById(
            @PathVariable @Min(0) Integer compId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение подбороки событий с id {}",
                request.getRemoteAddr(), request.getRequestURI(), compId);
        return compilationService.getCompilationById(compId);
    }
}
