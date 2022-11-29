package ru.practicum.ewm.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    CompilationDto addCompilation(@RequestBody @Valid CompilationNewDto compilationNewDto) {
        return compilationService.addCompilation(compilationNewDto);
    }

    @DeleteMapping("/{compId}")
    void deleteCompilation(@PathVariable @Min(0) Integer compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    void deleteCompilationEvent(@PathVariable @Min(0) Integer compId,
                                @PathVariable @Min(0) Integer eventId) {
        compilationService.deleteCompilationEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    void addCompilationEvent(@PathVariable @Min(0) Integer compId,
                             @PathVariable @Min(0) Integer eventId) {
        compilationService.addCompilationEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    void unpinCompilation(@PathVariable @Min(0) Integer compId) {
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    void pinCompilation(@PathVariable @Min(0) Integer compId) {
        compilationService.pinCompilation(compId);
    }
}
