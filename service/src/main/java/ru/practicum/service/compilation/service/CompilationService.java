package ru.practicum.service.compilation.service;

import ru.practicum.service.compilation.dto.CompilationDto;
import ru.practicum.service.compilation.dto.CompilationNewDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Integer compId);

    CompilationDto addCompilation(CompilationNewDto compilationNewDto);

    void deleteCompilation(Integer compId);

    void deleteCompilationEvent(Integer compId, Integer eventId);

    void addCompilationEvent(Integer compId, Integer eventId);

    void unpinCompilation(Integer compId);

    void pinCompilation(Integer compId);
}
