package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteServerException extends RuntimeException {
    public RemoteServerException(final String message) {
        super(message);
        log.error(message);
    }
}
