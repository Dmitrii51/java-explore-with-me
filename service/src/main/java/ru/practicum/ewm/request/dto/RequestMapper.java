package ru.practicum.ewm.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.model.Request;

@UtilityClass
public class RequestMapper {

    public RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getEvent().getId(),
                request.getCreated(),
                request.getRequester().getId(),
                request.getStatus());
    }
}
