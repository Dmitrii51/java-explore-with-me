package ru.practicum.ewm.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public CommentDtoFull toCommentFullDto(Comment comment) {
        return new CommentDtoFull(comment.getId(),
                comment.getText(),
                UserMapper.toUserDto(comment.getAuthor()),
                EventMapper.toEventDtoForComment(comment.getEvent()),
                comment.getCreated(),
                comment.getEdited(),
                getStatus(comment)
        );
    }

    public Comment fromCommentNewDto(CommentNewDto commentNewDto, Event event, User author) {
        return new Comment(
                null,
                commentNewDto.getText(),
                event,
                author,
                LocalDateTime.now(),
                null,
                CommentStatus.PENDING
        );
    }

    public Comment fromCommentUpdateDto(CommentUpdateDto commentUpdateDto, Event event,
                                        User author, CommentStatus status, LocalDateTime created) {
        return new Comment(
                commentUpdateDto.getCommentId(),
                commentUpdateDto.getText(),
                event,
                author,
                created,
                LocalDateTime.now(),
                status
        );
    }

    public CommentDtoShort toCommentDtoShort(Comment comment) {
        return new CommentDtoShort(
                comment.getId(),
                comment.getText(),
                comment.getEvent().getId(),
                comment.getCreated(),
                getStatus(comment)
        );
    }

    private CommentStatus getStatus(Comment comment) {
        return (comment.getEdited() != null && comment.getStatus().equals(CommentStatus.APPROVED))
                ? CommentStatus.EDITED
                : comment.getStatus();
    }
}
