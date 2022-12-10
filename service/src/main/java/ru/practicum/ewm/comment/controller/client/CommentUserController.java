package ru.practicum.ewm.comment.controller.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentDtoShort;
import ru.practicum.ewm.comment.dto.CommentNewDto;
import ru.practicum.ewm.comment.dto.CommentUpdateDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Validated
public class CommentUserController {

    private final CommentService commentService;

    @PostMapping
    CommentDto addComment(
            @RequestBody @Valid CommentNewDto commentNewDto,
            @PathVariable @Min(0) Integer userId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на добавление нового комментария пользователем с id {}",
                request.getRemoteAddr(), request.getRequestURI(), userId);
        return commentService.addComment(commentNewDto, userId);
    }

    @PatchMapping
    CommentDto updateComment(
            @RequestBody @Valid CommentUpdateDto commentUpdateDto,
            @PathVariable @Min(0) Integer userId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на изменение комментария c id = {}, добавленного пользователем с id {}",
                request.getRemoteAddr(), request.getRequestURI(), commentUpdateDto.getCommentId(), userId);
        return commentService.updateComment(commentUpdateDto, userId);
    }

    @GetMapping("/{commentId}")
    CommentDto getUserComment(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer commentId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение информации о комментарии с id {} пользователя с id {}",
                request.getRemoteAddr(), request.getRequestURI(), commentId, userId);
        return commentService.getUserComment(userId, commentId);
    }

    @GetMapping
    List<CommentDtoShort> getUserComments(
            @PathVariable @Min(0) Integer userId,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение комментариев, добавленных пользователем с id {}",
                request.getRemoteAddr(), request.getRequestURI(), userId);
        return commentService.getUserComments(userId, from, size);
    }
}
