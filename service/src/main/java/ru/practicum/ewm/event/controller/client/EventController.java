package ru.practicum.ewm.event.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.category.service.CategoryService;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventNewDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;
    private final CategoryService categoryService;

    @GetMapping("/events")
    List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") SortOption sort,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/events/{id}")
    EventDto getEventById(
            @PathVariable(name = "id") @Min(0) Integer eventId,
            HttpServletRequest request) {
        return eventService.getEventById(eventId, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/users/{userId}/events")
    List<EventShortDto> getUserEvents(
            @PathVariable @Min(0) Integer userId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    EventDto updateEvent(
            @RequestBody @Valid EventUpdateDto eventUpdate,
            @PathVariable @Min(0) Integer userId) {
        return eventService.updateEvent(userId, eventUpdate);
    }

    @PostMapping("/users/{userId}/events")
    EventDto addEvent(
            @RequestBody @Valid EventNewDto eventNew,
            @PathVariable @Min(0) Integer userId) {
        return eventService.addEvent(userId, eventNew);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    EventDto getUserEvent(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    EventDto cancelEvent(
            @PathVariable @Min(1) Integer userId,
            @PathVariable @Min(1) Integer eventId) {
        return eventService.cancelEvent(userId, eventId);
    }
}
