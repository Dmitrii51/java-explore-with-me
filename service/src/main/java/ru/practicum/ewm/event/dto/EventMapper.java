package ru.practicum.ewm.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.category.dto.CategoryMapper;
import ru.practicum.ewm.event.category.model.Category;
import ru.practicum.ewm.event.location.dto.LocationMapper;
import ru.practicum.ewm.event.location.model.Location;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public EventDto toEventDto(Event event, Integer confirmedRequests, Long views) {
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

    public EventShortDto toEventShortDto(Event event, Integer confirmedRequests, Long views) {
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

    public Event fromEventNewDto(EventNewDto eventNew, User initiator, Category category, Location location) {
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

    public EventDtoForComment toEventDtoForComment(Event event) {
        return new EventDtoForComment(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getCreated(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserDtoShort(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublished(),
                event.getState());
    }
}