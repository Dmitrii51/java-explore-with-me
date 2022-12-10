package ru.practicum.ewm.comment.controller.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDtoShort;
import ru.practicum.ewm.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
@Validated
public class CommentEventController {

    private final CommentService commentService;

    @GetMapping
    List<CommentDtoShort> getEventComments(
            @PathVariable @Min(0) Integer eventId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение комментариев к событию с id {}",
                request.getRemoteAddr(), request.getRequestURI(), eventId);
        return commentService.getEventComments(eventId, from, size);
    }
}
