package ru.practicum.service.event.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.category.dto.CategoryDto;
import ru.practicum.service.event.category.dto.NewCategoryDto;
import ru.practicum.service.event.category.service.CategoryService;
import ru.practicum.service.event.dto.EventDto;
import ru.practicum.service.event.dto.EventUpdateAdminDto;
import ru.practicum.service.event.model.EventState;
import ru.practicum.service.event.service.EventService;

import javax.validation.Valid;
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
    List<EventDto> getEventsAdmin(
            @RequestParam(name = "users", required = false) List<Integer> userIds,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false, name = "categories") List<Integer> categoryIds,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsAdmin(userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/events/{eventId}")
    EventDto updateEventAdmin(
            @RequestBody EventUpdateAdminDto eventUpdate,
            @PathVariable Integer eventId) {
        return eventService.updateEventAdmin(eventId, eventUpdate);
    }

    @PatchMapping("/events/{eventId}/publish")
    EventDto publishEvent(@PathVariable @Min(0) Integer eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    EventDto cancelEventAdmin(@PathVariable @Min(0) Integer eventId) {
        return eventService.cancelEventAdmin(eventId);
    }

    @PatchMapping("/categories")
    CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/categories")
    CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    void deleteCategory(@PathVariable @Min(0) Integer catId) {
        categoryService.deleteCategory(catId);
    }
}
