package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentDtoShort;
import ru.practicum.ewm.comment.dto.CommentNewDto;
import ru.practicum.ewm.comment.dto.CommentUpdateDto;
import ru.practicum.ewm.comment.model.CommentStatus;

import java.util.List;

public interface CommentService {

    CommentDto verifyCommentByAdmin(Integer commentId, CommentStatus status);

    void deleteCommentByAdmin(Integer commentId);

    CommentDto addComment(CommentNewDto commentNewDto, Integer userId);

    CommentDto updateComment(CommentUpdateDto commentUpdateDto, Integer userId);

    CommentDto getUserComment(Integer userId, Integer commentId);

    List<CommentDtoShort> getUserComments(Integer userId, Integer from, Integer size);

    List<CommentDtoShort> getEventComments(Integer eventId, Integer from, Integer size);
}
