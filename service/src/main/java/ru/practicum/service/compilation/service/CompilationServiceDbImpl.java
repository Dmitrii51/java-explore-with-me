package ru.practicum.service.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.service.compilation.dto.CompilationDto;
import ru.practicum.service.compilation.dto.CompilationMapper;
import ru.practicum.service.compilation.dto.CompilationNewDto;
import ru.practicum.service.compilation.model.Compilation;
import ru.practicum.service.compilation.repository.CompilationRepository;
import ru.practicum.service.event.dto.EventMapper;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.repository.EventRepository;
import ru.practicum.service.event.service.EventService;
import ru.practicum.service.exception.ResourceNotFoundException;
import ru.practicum.service.request.service.RequestService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceDbImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final RequestService requestService;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size)).toList();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        }

        return compilations.stream().map(compilation -> CompilationMapper.toCompilationDto(
                compilation, compilation.getEvents()
                        .stream().map(event -> EventMapper.toEventShortDto(event,
                                requestService.getConfirmedRequests(Math.toIntExact(event.getId())),
                                eventService.getEventListViews()))
                        .collect(Collectors.toList()))).collect(Collectors.toList());

    }

    @Override
    public CompilationDto getCompilationById(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId)));

        return CompilationMapper.toCompilationDto(compilation, compilation.getEvents().stream()
                .map(event -> EventMapper.toEventShortDto(event, requestService.getConfirmedRequests(event.getId()),
                        eventService.getEventListViews()))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(CompilationNewDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());

        Compilation compilation = compilationRepository.save(new Compilation(null,
                compilationDto.getPinned(), compilationDto.getTitle(), events));

        log.trace("{} Compilation id={} added : {}", LocalDateTime.now(),
                compilation.getId(), compilation);

        return CompilationMapper.toCompilationDto(compilation,
                events.stream().map(event -> EventMapper.toEventShortDto(event,
                                requestService.getConfirmedRequests(event.getId()), eventService.getEventListViews()))
                        .collect(Collectors.toList()));
    }

    @Override
    public void deleteCompilation(Integer compId) {
        compilationRepository.deleteById(compId);
        log.trace("{} Compilation id={} deleted", LocalDateTime.now(), compId);
    }

    @Override
    public void deleteCompilationEvent(Integer compId, Integer eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId)));

        compilation.getEvents().remove(eventService.getEventById(eventId));
        compilationRepository.save(compilation);

        log.trace("{} Event id={} removed from compilation id={} : {}",
                LocalDateTime.now(), eventId, compId, compilation);
    }

    @Override
    public void addCompilationEvent(Integer compId, Integer eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId)));

        compilation.getEvents().add(eventService.getEventById(eventId));
        compilationRepository.save(compilation);

        log.trace("{} Event id={} added to compilation id={} : {}",
                LocalDateTime.now(), eventId, compId, compilation);
    }

    @Override
    public void unpinCompilation(Integer compId) {
        if (compilationRepository.pinUnpinCompilation(compId, false) != 1) {
            throw new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId));
        }

        log.trace("{} Compilation id={} unpinned", LocalDateTime.now(), compId);
    }

    @Override
    public void pinCompilation(Integer compId) {
        if (compilationRepository.pinUnpinCompilation(compId, true) != 1) {
            throw new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId));
        }

        log.trace("{} Compilation id={} pinned", LocalDateTime.now(), compId);
    }
}
