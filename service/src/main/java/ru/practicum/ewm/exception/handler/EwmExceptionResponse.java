package ru.practicum.ewm.exception.handler;

public class EwmExceptionResponse {

    private final String error;

    public EwmExceptionResponse(String error) {
        this.error = error;
    }

    public String getExceptionMessage() {
        return error;
    }
}
