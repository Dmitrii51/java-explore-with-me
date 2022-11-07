package ru.practicum.service.compilation.dto;

import ru.practicum.service.compilation.model.Compilation;
import ru.practicum.service.event.dto.EventShortDto;

import java.util.List;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                events,
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }
}
