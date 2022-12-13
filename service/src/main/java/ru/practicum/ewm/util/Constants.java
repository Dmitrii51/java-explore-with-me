package ru.practicum.ewm.util;

import ru.practicum.ewm.comment.model.CommentStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Constants {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static final int MIN_COMMENT_LENGTH = 10;

    public static final int MAX_COMMENT_LENGTH = 3000;

    public static final List<CommentStatus> forbiddenStatusForSearch = List.of(
            CommentStatus.PENDING,
            CommentStatus.REJECTED,
            CommentStatus.HIDDEN);
}
