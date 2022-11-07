package ru.practicum.service.event.service;

import ru.practicum.service.event.controller.client.SortOption;
import ru.practicum.service.event.dto.*;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getEvents(
            String text, List<Integer> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, SortOption sort, Integer from,
            Integer size, String ip, String uri);

    EventDto getEventById(Integer eventId, String ip, String uri);

    List<EventShortDto> getUserEvents(Integer userId, Integer from, Integer size);

    EventDto updateEvent(Integer userId, EventUpdateDto eventUpdate);

    EventDto addEvent(Integer userId, EventNewDto eventNew);

    EventDto getUserEvent(Integer userId, Integer eventId);

    EventDto cancelEvent(Integer userId, Integer eventId);

    List<EventDto> getEventsAdmin(
            List<Integer> userIds, List<EventState> states, List<Integer> categoryIds,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventDto updateEventAdmin(Integer eventId, EventUpdateAdminDto eventUpdate);

    EventDto publishEvent(Integer eventId);

    EventDto cancelEventAdmin(Integer eventId);

    Event getEventById(Integer eventId);

    Integer getEventListViews();
}
