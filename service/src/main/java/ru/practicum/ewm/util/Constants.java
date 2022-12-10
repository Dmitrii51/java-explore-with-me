package ru.practicum.ewm.util;

import ru.practicum.ewm.comment.model.CommentStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Constants {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public final static int MIN_COMMENT_LENGTH = 10;

    public final static int MAX_COMMENT_LENGTH = 3000;

    public final static List<CommentStatus> forbiddenStatusForSearch = List.of(
            CommentStatus.PENDING,
            CommentStatus.REJECTED,
            CommentStatus.HIDDEN);
}
