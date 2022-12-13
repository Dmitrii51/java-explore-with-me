package ru.practicum.ewm.comment.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDtoFull;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@Validated
public class CommentAdminController {

    private final CommentService commentService;

    @PutMapping("/{commentId}")
    CommentDtoFull verifyCommentByAdmin(
            @PathVariable @Min(0) Integer commentId,
            @RequestParam(required = false, defaultValue = "APPROVED") CommentStatus status,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на изменения статуса комменария с id {}",
                request.getRemoteAddr(), request.getRequestURI(), commentId);
        return commentService.verifyCommentByAdmin(commentId, status);
    }

    @DeleteMapping("/{commentId}")
    void deleteCommentByAdmin(
            @PathVariable @Min(0) Integer commentId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на удаление комментария с id {}",
                request.getRemoteAddr(), request.getRequestURI(), commentId);
        commentService.deleteCommentByAdmin(commentId);
    }

    @GetMapping("/user/{authorId}")
    public List<CommentDtoFull> getAuthorComments(
            @PathVariable @Min(0) Integer authorId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на получение комментариев автора с id {}",
                request.getRemoteAddr(), request.getRequestURI(), authorId);
        return commentService.getAuthorCommentsByAdmin(authorId, from, size);
    }

    @GetMapping("/event/{eventId}")
    public List<CommentDtoFull> getEventComments(
            @PathVariable @Min(0) Integer eventId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на получение комментариев к событию с id {}",
                request.getRemoteAddr(), request.getRequestURI(), eventId);
        return commentService.getEventCommentsByAdmin(eventId, from, size);
    }
}
