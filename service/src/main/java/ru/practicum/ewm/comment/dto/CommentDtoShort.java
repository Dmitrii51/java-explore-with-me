package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.comment.model.CommentStatus;
import ru.practicum.ewm.util.Constants;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoShort {

    private Integer id;

    private String text;

    private Integer eventId;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime created;

    private CommentStatus status;
}
