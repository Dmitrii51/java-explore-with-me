package ru.practicum.ewm.event.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.category.service.CategoryService;
import ru.practicum.ewm.event.controller.client.SortOption;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.location.dto.LocationMapper;
import ru.practicum.ewm.event.location.model.Location;
import ru.practicum.ewm.event.location.repository.LocationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.ResourceNotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.stats.StatsClient;
import ru.practicum.ewm.stats.dto.StatsPostRequestDto;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceDbImpl implements EventService {

    private final Gson gsonForClient;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final LocationRepository locationRepository;
    private final RequestService requestService;
    @Value("${EWM_STATS_URL}")
    private String uriServer;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public EventServiceDbImpl(
            UserService userService, EventRepository eventRepository, CategoryService categoryService,
            LocationRepository locationRepository, @Lazy RequestService requestService) {
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
            rangeEnd = LocalDateTime.now().plusYears(10);
        }

        List<Event> events = eventRepository.getPublishedEvents(
                text != null ? text.toLowerCase() : null,
                categories, paid, rangeStart, rangeEnd,
                PageRequest.of(from / size, size, Sort.Direction.ASC, "eventDate"));

        List<EventShortDto> eventsDto = events.stream()
                .map(event -> EventMapper.toEventShortDto(
                        event, getConfirmedRequests(event.getId()), getEventListViews()))
                .collect(Collectors.toList());

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
            throw new ResourceNotFoundException(String.format("События с id = %s не было опубликовано", eventId));
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
                        getEventViews(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEvent(Integer userId, EventUpdateDto eventUpdate) {
        Event event = getEventById(eventUpdate.getEventId());
        validateEventBeforeUpdate(userId, event.getId(), event);
        updateEventData(eventUpdate, event);
        eventRepository.save(event);
        log.info("Изменение информации о событии с id {}", event.getId());
        return EventMapper.toEventDto(event, getConfirmedRequests(event.getId()), getEventViews(event.getId()));
    }

    private void validateEventBeforeUpdate(Integer userId, Integer eventId, Event event) {
        validateEventStatus(event);
        validateEventInitiator(userId, eventId, event);
        validateEventDate(event.getEventDate());
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (!eventDate.isAfter(LocalDateTime.now().withNano(0).plusHours(2))) {
            throw new ForbiddenException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
    }

    private void updateEventData(EventUpdateDto eventUpdate, Event event) {
        if (eventUpdate.getEventDate() != null) {
            event.setEventDate(eventUpdate.getEventDate());
        }

        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        if (eventUpdate.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(eventUpdate.getCategory()));
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

    @Override
    public EventDto addEvent(Integer userId, EventNewDto eventNew) {
        validateEventDate(eventNew.getEventDate());
        Location location = locationRepository.getByLatitudeAndLongitude(
                        eventNew.getLocation().getLat(), eventNew.getLocation().getLon())
                .orElseGet(() -> locationRepository.save(LocationMapper.fromLocationDto(eventNew.getLocation())));
        Event event = eventRepository.save(EventMapper.fromEventNewDto(
                eventNew, userService.getUserById(userId),
                categoryService.getCategoryById(eventNew.getCategory()), location));
        log.info("Добавление нового события c id {}", event.getId());
        return EventMapper.toEventDto(event, 0, 0L);
    }

    @Override
    public EventDto getUserEvent(Integer userId, Integer eventId) {
        Event event = getEventById(eventId);
        validateEventInitiator(userId, eventId, event);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    @Override
    public EventDto cancelEvent(Integer userId, Integer eventId) {
        Event event = getEventById(eventId);
        validateEventBeforeUserRejection(userId, eventId, event);
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    private void validateEventBeforeUserRejection(Integer userId, Integer eventId, Event event) {
        validateEventStatus(event);
        validateEventInitiator(userId, eventId, event);
    }

    private void validateEventInitiator(Integer userId, Integer eventId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException(
                    String.format("Пользователь с id = %s не является инициатором события с id = %s",
                            userId, eventId));
        }
    }

    @Override
    public List<EventDto> getEventsByAdmin(
            List<Integer> userIds, List<EventState> states,
            List<Integer> categoryIds, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Integer from, Integer size) {

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(10);
        }

        List<Event> events = eventRepository.getEventsByAdmin(
                userIds, states, categoryIds,
                rangeStart, rangeEnd, PageRequest.of(from / size, size));

        return events.stream()
                .map(event -> EventMapper.toEventDto(
                        event, getConfirmedRequests(event.getId()), getEventListViews()))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEventByAdmin(Integer eventId, EventUpdateAdminDto eventUpdate) {
        Event event = getEventById(eventId);
        updateEventDataByAdmin(eventUpdate, event);
        eventRepository.save(event);
        log.info("Изменение информации о событии с id {}", event.getId());
        return EventMapper.toEventDto(event, getConfirmedRequests(
                event.getId()), getEventViews(event.getId()));
    }

    private void updateEventDataByAdmin(EventUpdateAdminDto eventUpdate, Event event) {

        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        if (eventUpdate.getDescription() != null) {
            event.setDescription(eventUpdate.getDescription());
        }

        if (eventUpdate.getEventDate() != null) {
            event.setEventDate(eventUpdate.getEventDate());
        }

        if (eventUpdate.getLocation() != null) {
            Location location = locationRepository.getByLatitudeAndLongitude(
                            eventUpdate.getLocation().getLat(), eventUpdate.getLocation().getLon())
                    .orElseGet(() -> locationRepository.save(
                            LocationMapper.fromLocationDto(eventUpdate.getLocation())));
            event.setLocation(location);
        }

        if (eventUpdate.getPaid() != null) {
            event.setPaid(eventUpdate.getPaid());
        }

        if (eventUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdate.getParticipantLimit());
        }

        if (eventUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdate.getRequestModeration());
        }

        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }

        if (eventUpdate.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(eventUpdate.getCategory()));
        }
    }

    @Override
    public EventDto publishEventByAdmin(Integer eventId) {
        Event event = getEventById(eventId);
        validateEventBeforePublishing(event);
        event.setState(EventState.PUBLISHED);
        event.setPublished(LocalDateTime.now());
        eventRepository.save(event);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    private void validateEventBeforePublishing(Event event) {
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException("Событие должно быть в состоянии ожидания публикации");
        }

        if (event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }
    }

    @Override
    public EventDto cancelEventByAdmin(Integer eventId) {
        Event event = getEventById(eventId);
        validateEventStatus(event);
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return EventMapper.toEventDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
    }

    private void validateEventStatus(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Опубликованное событие не может быть изменено");
        }
    }

    private Integer getConfirmedRequests(Integer eventId) {
        return requestService.getConfirmedRequests(eventId);
    }

    @Override
    public Event getEventById(Integer eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("События с id = %s не существует", eventId)));
    }

    private void sendStatistics(String ip, String uri) {
        StatsPostRequestDto statsDto = new StatsPostRequestDto(
                null, "service", uri, ip, LocalDateTime.now().format(DATE_TIME_FORMATTER));
        StatsClient.sendStatistics(uriServer, gsonForClient.toJson(statsDto));
    }

    private Long getEventViews(Integer eventId) {
        return StatsClient.getViews(uriServer, List.of("/events/" + eventId), false);
    }

    public Long getEventListViews() {
        return StatsClient.getViews(uriServer, List.of("/events"), false);
    }
}
