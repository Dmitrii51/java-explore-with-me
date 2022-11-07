package ru.practicum.service.request.dto;

import ru.practicum.service.request.model.Request;

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
