package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDtoFull;
import ru.practicum.ewm.comment.dto.CommentDtoShort;
import ru.practicum.ewm.comment.dto.CommentNewDto;
import ru.practicum.ewm.comment.dto.CommentUpdateDto;
import ru.practicum.ewm.comment.model.CommentStatus;

import java.util.List;

public interface CommentService {

    CommentDtoFull verifyCommentByAdmin(Integer commentId, CommentStatus status);

    void deleteCommentByAdmin(Integer commentId);

    CommentDtoShort addComment(CommentNewDto commentNewDto, Integer userId);

    CommentDtoShort updateComment(CommentUpdateDto commentUpdateDto, Integer userId);

    void deleteCommentByUser(Integer commentId, Integer userId);

    CommentDtoShort getUserComment(Integer userId, Integer commentId);

    List<CommentDtoShort> getUserComments(Integer userId, Integer from, Integer size);

    List<CommentDtoShort> getEventComments(Integer eventId, Integer from, Integer size);

    List<CommentDtoFull> getAuthorCommentsByAdmin(Integer authorId, Integer from, Integer size);

    List<CommentDtoFull> getEventCommentsByAdmin(Integer eventId, Integer from, Integer size);
}
