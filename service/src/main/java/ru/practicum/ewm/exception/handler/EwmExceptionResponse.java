package ru.practicum.ewm.exception.handler;

public class EwmExceptionResponse {

    private final String exceptionMessage;

    public EwmExceptionResponse(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
