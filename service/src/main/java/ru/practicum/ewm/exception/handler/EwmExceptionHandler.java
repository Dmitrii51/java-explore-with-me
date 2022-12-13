package ru.practicum.ewm.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.*;

import java.sql.SQLException;

@RestControllerAdvice
public class EwmExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public EwmExceptionResponse handleConflictException(final ConflictException e) {
        return new EwmExceptionResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public EwmExceptionResponse handleSQLException(final SQLException e) {
        return new EwmExceptionResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public EwmExceptionResponse handleForbiddenException(final ForbiddenException e) {
        return new EwmExceptionResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EwmExceptionResponse handleRemoteServerException(final RemoteServerException e) {
        return new EwmExceptionResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EwmExceptionResponse handleResourceNotFoundException(final ResourceNotFoundException e) {
        return new EwmExceptionResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EwmExceptionResponse handleValidationException(final ValidationException e) {
        return new EwmExceptionResponse(e.getMessage());
    }
}
