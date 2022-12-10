package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.event.dto.EventDtoForComment;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.util.Constants;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoFull {

    private Integer id;

    private String text;

    private UserDto author;

    private EventDtoForComment event;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime created;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime updated;

    private CommentStatus status;
}
