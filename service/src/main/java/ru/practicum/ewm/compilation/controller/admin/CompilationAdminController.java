package ru.practicum.ewm.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    CompilationDto addCompilation(
            @RequestBody @Valid CompilationNewDto compilationNewDto,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на добавление новой подборки событий",
                request.getRemoteAddr(), request.getRequestURI());
        return compilationService.addCompilation(compilationNewDto);
    }

    @DeleteMapping("/{compId}")
    void deleteCompilation(
            @PathVariable @Min(0) Integer compId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на удаление подборки событий с id {}",
                request.getRemoteAddr(), request.getRequestURI(), compId);
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    void deleteCompilationEvent(
            @PathVariable @Min(0) Integer compId,
            @PathVariable @Min(0) Integer eventId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на удаление " +
                        "события c id {} из подборки событий с id {}",
                request.getRemoteAddr(), request.getRequestURI(), eventId, compId);
        compilationService.deleteCompilationEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    void addCompilationEvent(
            @PathVariable @Min(0) Integer compId,
            @PathVariable @Min(0) Integer eventId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на добавление " +
                        "события c id {} в подборку событий с id {}",
                request.getRemoteAddr(), request.getRequestURI(), eventId, compId);
        compilationService.addCompilationEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    void unpinCompilation(
            @PathVariable @Min(0) Integer compId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на открепление " +
                        "подборки событий c id {} с главной страницы",
                request.getRemoteAddr(), request.getRequestURI(), compId);
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    void pinCompilation(
            @PathVariable @Min(0) Integer compId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на закрепление " +
                        "подборки событий c id {} на главной странице",
                request.getRemoteAddr(), request.getRequestURI(), compId);
        compilationService.pinCompilation(compId);
    }
}
