package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.CompilationNewDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.ResourceNotFoundException;
import ru.practicum.ewm.request.service.RequestService;

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
        List<Compilation> compilations = (pinned == null)
                ? compilationRepository.findAll(PageRequest.of(from / size, size)).toList()
                : compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        return compilations.stream()
                .map(compilation -> CompilationMapper.toCompilationDto(
                        compilation,
                        getCompilationEventShortDtoList(compilation.getEvents())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId)));
        return CompilationMapper.toCompilationDto(
                compilation,
                getCompilationEventShortDtoList(compilation.getEvents()));
    }

    @Override
    public CompilationDto addCompilation(CompilationNewDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        Compilation compilation = compilationRepository.save(
                new Compilation(null, compilationDto.getPinned(), compilationDto.getTitle(), events));
        log.info("Добавление новой подборки событий c id = {}", compilation.getId());
        return CompilationMapper.toCompilationDto(
                compilation,
                getCompilationEventShortDtoList(events));
    }

    private List<EventShortDto> getCompilationEventShortDtoList(List<Event> events) {
        return events.stream()
                .map(event -> EventMapper.toEventShortDto(
                        event,
                        requestService.getConfirmedRequests(event.getId()),
                        eventService.getEventListViews()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCompilation(Integer compId) {
        compilationRepository.deleteById(compId);
        log.info("Удаление подборки событий c id = {}", compId);
    }

    @Override
    public void deleteCompilationEvent(Integer compId, Integer eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId)));
        compilation.getEvents().remove(eventService.getEventById(eventId));
        compilationRepository.save(compilation);
        log.info("Удаление события с id = {} из подборки c id = {}", eventId, compId);
    }

    @Override
    public void addCompilationEvent(Integer compId, Integer eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId)));
        compilation.getEvents().add(eventService.getEventById(eventId));
        compilationRepository.save(compilation);
        log.info("Добавление события с id = {} в подборку c id = {}", eventId, compId);
    }

    @Override
    public void unpinCompilation(Integer compId) {
        if (compilationRepository.pinUnpinCompilation(compId, false) != 1) {
            log.warn("Попытка открепления несуществующей подборки c id = {} с главной страницы", compId);
            throw new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId));
        }
        log.info("Открепление подборки c id {} с главной страницы", compId);
    }

    @Override
    public void pinCompilation(Integer compId) {
        if (compilationRepository.pinUnpinCompilation(compId, true) != 1) {
            log.warn("Попытка закрепления несуществующей подборки c id = {} на главной странице", compId);
            throw new ResourceNotFoundException(String.format("Подборки событий с id = %s не существует", compId));
        }
        log.info("Закрепление подборки c id = {} на главной странице", compId);
    }
}
