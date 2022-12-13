package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getUserEventRequests(Integer userId, Integer eventId);

    RequestDto confirmRequest(Integer userId, Integer eventId, Integer reqId);

    RequestDto rejectRequest(Integer userId, Integer eventId, Integer reqId);

    List<RequestDto> getUserRequests(Integer userId);

    RequestDto addRequest(Integer userId, Integer eventId);

    RequestDto cancelRequest(Integer userId, Integer requestId);

    Integer getConfirmedRequests(Integer eventId);
}
