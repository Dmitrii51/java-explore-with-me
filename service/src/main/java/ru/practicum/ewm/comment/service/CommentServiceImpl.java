package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.ResourceNotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.util.Constants;
import ru.practicum.ewm.util.PageBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;

    private Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Комментария с id = %s не существует", commentId)));
    }

    @Override
    public List<CommentDtoShort> getEventComments(Integer eventId, Integer from, Integer size) {
        Pageable page = PageBuilder.getPage(from, size, "created", Sort.Direction.ASC);
        return commentRepository.findEventComments(eventId, Constants.forbiddenStatusForSearch, page).stream()
                .map(CommentMapper::toCommentDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDtoShort getUserComment(Integer userId, Integer commentId) {
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(comment.getAuthor(), userId, commentId);
        return CommentMapper.toCommentDtoShort(comment);
    }

    @Override
    public List<CommentDtoShort> getUserComments(Integer userId, Integer from, Integer size) {
        Pageable page = PageBuilder.getPage(from, size, "created", Sort.Direction.ASC);
        return commentRepository.findAllByAuthorId(userId, CommentStatus.REJECTED, page).stream()
                .map(CommentMapper::toCommentDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDtoShort addComment(CommentNewDto commentNewDto, Integer userId) {
        Event event = eventService.getEventById(commentNewDto.getEventId());
        validateEventStatusForComment(event);
        Comment comment = commentRepository.save(CommentMapper.fromCommentNewDto(
                commentNewDto, event, userService.getUserById(userId)));
        log.info("Добавление нового комментария c id {}", event.getId());
        return CommentMapper.toCommentDtoShort(comment);
    }

    private void validateEventStatusForComment(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Добавлять комментарии можно только к опубликованным событиям");
        }
    }

    @Override
    @Transactional
    public CommentDtoShort updateComment(CommentUpdateDto commentUpdateDto, Integer userId) {
        Comment comment = getCommentById(commentUpdateDto.getCommentId());
        validateCommentStatusBeforeUpdate(comment);
        User author = comment.getAuthor();
        validateCommentAuthor(author, userId, comment.getId());
        Comment updatedComment = commentRepository.save(CommentMapper.fromCommentUpdateDto(
                commentUpdateDto, comment.getEvent(), author, CommentStatus.PENDING, comment.getCreated()));
        log.info("Изменение комментария c id {}", comment.getId());
        return CommentMapper.toCommentDtoShort(updatedComment);
    }

    private void validateCommentStatusBeforeUpdate(Comment comment) {
        if (comment.getStatus().equals(CommentStatus.REJECTED)) {
            throw new ForbiddenException("Нельзя изменять комментарий, который был отклонен модератором");
        }
    }

    private void validateCommentAuthor(User author, Integer userId, Integer commentId) {
        if (!author.getId().equals(userId)) {
            throw new ForbiddenException(
                    String.format("Пользователь с id = %s не является автором комментария с id = %s",
                            userId, commentId));
        }
    }

    @Override
    @Transactional
    public void deleteCommentByUser(Integer commentId, Integer userId) {
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(comment.getAuthor(), userId, commentId);
        commentRepository.deleteById(commentId);
        log.info("Удаление комментария с id {} пользователем с id {}", commentId, userId);
    }

    @Override
    @Transactional
    public CommentDtoFull verifyCommentByAdmin(Integer commentId, CommentStatus status) {
        Comment comment = getCommentById(commentId);
        commentRepository.updateCommentStatus(commentId, status);
        comment.setStatus(status);
        return CommentMapper.toCommentFullDto(comment);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Integer commentId) {
        getCommentById(commentId);
        commentRepository.deleteById(commentId);
        log.info("Удаление комментария с id {} администраторм", commentId);
    }

    public List<CommentDtoFull> getAuthorCommentsByAdmin(Integer authorId, Integer from, Integer size) {
        Pageable page = PageBuilder.getPage(from, size, "created", Sort.Direction.ASC);
        return commentRepository.findAllByAuthorId(authorId, page).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    public List<CommentDtoFull> getEventCommentsByAdmin(Integer eventId, Integer from, Integer size) {
        Pageable page = PageBuilder.getPage(from, size, "created", Sort.Direction.ASC);
        return commentRepository.findAllByEventId(eventId, page).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }
}
