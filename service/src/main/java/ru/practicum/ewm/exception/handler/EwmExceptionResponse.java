package ru.practicum.ewm.exception.handler;

public class EwmExceptionResponse {

    private final String error;

    public EwmExceptionResponse(String exceptionMessage) {
        this.error = exceptionMessage;
    }

    public String getExceptionMessage() {
        return error;
    }
}
