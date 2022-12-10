package ru.practicum.ewm.comment.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments/{commentId}")
@Validated
public class CommentAdminController {

    private final CommentService commentService;

    @PutMapping
    CommentDto verifyCommentByAdmin(
            @PathVariable @Min(0) Integer commentId,
            @RequestParam(required = false, defaultValue = "APPROVED") CommentStatus status,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на изменения статуса комменария с id {}",
                request.getRemoteAddr(), request.getRequestURI(), commentId);
        return commentService.verifyCommentByAdmin(commentId, status);
    }

    @DeleteMapping
    void deleteCommentByAdmin(
            @PathVariable @Min(0) Integer commentId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на удаление комментария с id {}",
                request.getRemoteAddr(), request.getRequestURI(), commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
