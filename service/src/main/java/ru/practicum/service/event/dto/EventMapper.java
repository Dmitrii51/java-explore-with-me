package ru.practicum.service.event.dto;

import ru.practicum.service.event.category.dto.CategoryMapper;
import ru.practicum.service.event.category.model.Category;
import ru.practicum.service.event.location.dto.LocationMapper;
import ru.practicum.service.event.location.model.Location;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.EventState;
import ru.practicum.service.user.dto.UserMapper;
import ru.practicum.service.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static EventDto toEventDto(Event event, Integer confirmedRequests, Integer views) {
        return new EventDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getCreated(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserDtoShort(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublished(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                views);
    }

    public static EventShortDto toEventShortDto(Event event, Integer confirmedRequests, Integer views) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserDtoShort(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                views);
    }

    public static Event fromEventNewDto(EventNewDto eventNew, User initiator, Category category, Location location) {
        return new Event(
                null,
                eventNew.getAnnotation(),
                LocalDateTime.now(),
                eventNew.getDescription(),
                eventNew.getEventDate(), initiator,
                location, eventNew.getPaid(),
                eventNew.getParticipantLimit(),
                null,
                eventNew.getRequestModeration(),
                EventState.PENDING,
                eventNew.getTitle(),
                category
        );
    }
}
