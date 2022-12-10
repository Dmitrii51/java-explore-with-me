package ru.practicum.ewm.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment fromCommentDto(CommentDto commentDto) {
        return new Comment(
            commentDto.getId(),
            commentDto.getText(),
            commentDto.getEvent(),
            commentDto.getAuthor(),
            commentDto.getCreated(),
            commentDto.getStatus()
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
            comment.getText(),
            comment.getEvent(),
            comment.getAuthor(),
            comment.getCreated(),
            comment.getStatus()
        );
    }

    public Comment fromCommentNewDto(CommentNewDto commentNewDto, Event event, User author) {
        return new Comment(
            null,
            commentNewDto.getText(),
            event,
            author,
            LocalDateTime.now(),
            CommentStatus.PENDING
        );
    }

    public Comment fromCommentUpdateDto(CommentUpdateDto commentUpdateDto, Event event,
                                        User author, CommentStatus status) {
        return new Comment(
            commentUpdateDto.getCommentId(),
            commentUpdateDto.getText(),
            event,
            author,
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
            comment.getStatus()
        );
    }
}
