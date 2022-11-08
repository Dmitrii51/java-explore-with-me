package ru.practicum.service.event.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.service.client.StatsClient;
import ru.practicum.service.event.category.model.Category;
import ru.practicum.service.event.category.service.CategoryService;
import ru.practicum.service.event.controller.client.SortOption;
import ru.practicum.service.event.dto.*;
import ru.practicum.service.event.location.dto.LocationMapper;
import ru.practicum.service.event.location.model.Location;
import ru.practicum.service.event.location.repository.LocationRepository;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.EventState;
import ru.practicum.service.event.repository.EventRepository;
import ru.practicum.service.exception.ForbiddenException;
import ru.practicum.service.exception.ResourceNotFoundException;
import ru.practicum.service.exception.ValidationException;
import ru.practicum.service.request.service.RequestService;
import ru.practicum.service.user.service.UserService;
import ru.practicum.stats.dto.StatsPostRequestDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EventServiceDbImpl implements EventService {

    private final Gson gsonForClient;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final LocationRepository locationRepository;
    private final RequestService requestService;
    @Value("${ewm_stats_url}")
    private String uriServer;

    @Autowired
    public EventServiceDbImpl(
            UserService userService, EventRepository eventRepository, CategoryService categoryService,
            LocationRepository locationRepository, RequestService requestService) {
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
        this.locationRepository = locationRepository;
        this.requestService = requestService;
        this.gsonForClient = new Gson();
    }

    @Override
    public List<EventShortDto> getEvents(
            String text, List<Integer> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, SortOption sort, Integer from, Integer size,
            String ip, String uri) {

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }

        List<Event> events = eventRepository.getPublishedEvents(text != null ? text.toLowerCase() : null,
                categories, paid, rangeStart, rangeEnd, PageRequest.of(from / size, size, Sort.Direction.ASC,
                        "eventDate"));

        List<EventShortDto> eventsDto = events.stream().map(event -> EventMapper.toEventShortDto(
                event, getConfirmedRequests(Math.toIntExact(event.getId())),
                getEventListViews())).collect(Collectors.toList());

        if (onlyAvailable) {
            HashMap<Integer, Integer> limits = new HashMap<>();

            events.forEach(event -> limits.put(event.getId(), event.getParticipantLimit()));
            eventsDto = eventsDto.stream().filter(eventDto -> limits.get(eventDto.getId()).equals(0) ||
                    limits.get(eventDto.getId()) > eventDto.getConfirmedRequests()).collect(Collectors.toList());
        }
        if (sort.equals(SortOption.VIEWS)) {
            eventsDto.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        }
        sendStatistics(ip, uri);
        return eventsDto;
    }

    @Override
    public EventDto getEventById(Integer eventId, String ip, String uri) {
        Event event = getEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ResourceNotFoundException(String.format("События с id = %s не существует", eventId));
        }
        sendStatistics(ip, uri);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    @Override
    public List<EventShortDto> getUserEvents(Integer userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findAllByInitiatorId(
                userId, PageRequest.of(from / size, size));
        return events.stream().map(event -> EventMapper.toEventShortDto(
                        event, getConfirmedRequests(Math.toIntExact(event.getId())),
                        getEventViews(Math.toIntExact(event.getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEvent(Integer userId, EventUpdateDto eventUpdate) {
        Event event = checkConditions(userId, eventUpdate.getEventId());
        updateEventData(eventUpdate, event);
        eventRepository.save(event);
        log.trace("{} Event id={} updated : {}", LocalDateTime.now(), event.getId(), event);
        return EventMapper.toEventDto(event, getConfirmedRequests(
                Math.toIntExact(event.getId())), getEventViews(Math.toIntExact(event.getId())));
    }

    @Override
    public EventDto addEvent(Integer userId, EventNewDto eventNew) {
        Location location = locationRepository.getByLatAndLon(
                        eventNew.getLocation().getLatitude(), eventNew.getLocation().getLongitude())
                .orElseGet(() -> locationRepository.save(LocationMapper.fromLocationDto(eventNew.getLocation())));
        Event event = eventRepository.save(EventMapper.fromEventNewDto(
                eventNew, userService.getUserById(userId),
                getCatById(eventNew.getCategory()), location));
        log.info("Добавление нового события c id {}", event.getId());
        return EventMapper.toEventDto(event, 0, 0);
    }

    @Override
    public EventDto getUserEvent(Integer userId, Integer eventId) {
        Event event = checkConditions(userId, eventId);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    @Override
    public EventDto cancelEvent(Integer userId, Integer eventId) {
        Event event = checkConditions(userId, eventId);
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    @Override
    public List<EventDto> getEventsAdmin(
            List<Integer> userIds, List<EventState> states,
            List<Integer> categoryIds, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Integer from, Integer size) {

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }

        List<Event> events = eventRepository.getEventsAdmin(
                userIds, states, categoryIds,
                rangeStart, rangeEnd, PageRequest.of(from / size, size));

        return events.stream().map(event -> EventMapper.toEventDto(
                        event, getConfirmedRequests(Math.toIntExact(event.getId())),
                        getEventListViews()))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEventAdmin(Integer eventId, EventUpdateAdminDto eventUpdate) {
        Event event = getEventById(eventId);
        updateEventDataAdm(eventUpdate, event);
        eventRepository.save(event);
        log.info("Изменение информации о событии с id {}", event.getId());
        return EventMapper.toEventDto(event, getConfirmedRequests(
                Math.toIntExact(event.getId())), getEventViews(Math.toIntExact(event.getId())));
    }

    @Override
    public EventDto publishEvent(Integer eventId) {
        Event event = checkPublishConditions(eventId);
        event.setState(EventState.PUBLISHED);
        event.setPublished(LocalDateTime.now());
        eventRepository.save(event);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    @Override
    public EventDto cancelEventAdmin(Integer eventId) {
        Event event = checkConditionsAdm(eventId);
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    protected Integer getConfirmedRequests(Integer eventId) {
        return requestService.getConfirmedRequests(eventId);
    }

    protected void updateEventData(EventUpdateDto eventUpdate, Event event) {
        if (eventUpdate.getEventDate() != null) {
            event.setEventDate(eventUpdate.getEventDate());
        }

        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        if (eventUpdate.getCategory() != null) {
            event.setCategory(getCatById(eventUpdate.getCategory()));
        }

        if (eventUpdate.getPaid() != null) {
            event.setPaid(eventUpdate.getPaid());
        }

        if (eventUpdate.getDescription() != null) {
            event.setDescription(eventUpdate.getDescription());
        }

        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }

        if (eventUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdate.getParticipantLimit());
        }

        if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
        }
    }

    protected Event checkConditions(Integer userId, Integer eventId) {
        Event event = checkConditionsAdm(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    String.format("События с id = %s не существует", eventId));
        }
        return event;
    }

    protected Event checkConditionsAdm(Integer eventId) {
        Event event = getEventById(eventId);

        if (event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException("Опубликованные события не могут быть изменены");
        }
        return event;
    }

    protected Category getCatById(Integer catId) {
        return categoryService.getCatById(catId);
    }

    protected Event checkPublishConditions(Integer eventId) {
        Event event = getEventById(eventId);

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException("Только предстоящие события могут быть опубликованы");
        }

        if (event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Начало события не может быть ранее часа после публикации");
        }
        return event;
    }

    @Override
    public Event getEventById(Integer eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("События с id = %s не существует", eventId)));
    }


    protected void updateEventDataAdm(EventUpdateAdminDto eventUpdate, Event event) {
        if (eventUpdate.getEventDate() != null) {
            event.setEventDate(eventUpdate.getEventDate());
        }

        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        if (eventUpdate.getCategory() != null) {
            event.setCategory(getCatById(eventUpdate.getCategory()));
        }

        if (eventUpdate.getPaid() != null) {
            event.setPaid(eventUpdate.getPaid());
        }

        if (eventUpdate.getDescription() != null) {
            event.setDescription(eventUpdate.getDescription());
        }

        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }

        if (eventUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdate.getParticipantLimit());
        }

        if (eventUpdate.getLocation() != null) {
            Location location = locationRepository.getByLatAndLon(
                            eventUpdate.getLocation().getLatitude(), eventUpdate.getLocation().getLongitude())
                    .orElseGet(() -> locationRepository.save(
                            LocationMapper.fromLocationDto(eventUpdate.getLocation())));
            event.setLocation(location);
        }

        if (eventUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdate.getRequestModeration());
        }
    }

    protected void sendStatistics(String ip, String uri) {
        StatsPostRequestDto statsDto = new StatsPostRequestDto(
                null, "service", uri, ip, LocalDateTime.now());
        StatsClient.sendStatistics(uriServer, gsonForClient.toJson(statsDto));
    }

    protected Integer getEventViews(Integer eventId) {
        return StatsClient.getViews(uriServer, List.of("/events/" + eventId), false);
    }

    public Integer getEventListViews() {
        return StatsClient.getViews(uriServer, List.of("/events"), false);
    }
}
