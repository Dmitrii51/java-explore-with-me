package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.request.model.Request;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getEvent().getId(),
                request.getCreated(),
                request.getRequester().getId(),
                request.getStatus());
    }
}
