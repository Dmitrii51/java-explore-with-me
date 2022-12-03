package ru.practicum.ewm.event.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.category.service.CategoryService;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventUpdateAdminDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class EventAdminController {

    private final EventService eventService;
    private final CategoryService categoryService;

    @GetMapping("/events")
    List<EventDto> getEventsByAdmin(
            @RequestParam(name = "users", required = false) List<Integer> userIds,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false, name = "categories") List<Integer> categoryIds,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsByAdmin(userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/events/{eventId}")
    EventDto updateEventByAdmin(
            @RequestBody EventUpdateAdminDto eventUpdate,
            @PathVariable Integer eventId) {
        return eventService.updateEventByAdmin(eventId, eventUpdate);
    }

    @PatchMapping("/events/{eventId}/publish")
    EventDto publishEventByAdmin(@PathVariable @Min(0) Integer eventId) {
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    EventDto cancelEventByAdmin(@PathVariable @Min(0) Integer eventId) {
        return eventService.cancelEventByAdmin(eventId);
    }
}
