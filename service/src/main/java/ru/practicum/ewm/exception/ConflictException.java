package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConflictException extends RuntimeException {
    public ConflictException(final String message) {
        super(message);
        log.error(message);
    }
}
