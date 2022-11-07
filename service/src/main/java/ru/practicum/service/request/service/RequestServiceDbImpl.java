package ru.practicum.service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.model.EventState;
import ru.practicum.service.event.service.EventService;
import ru.practicum.service.exception.ForbiddenException;
import ru.practicum.service.exception.ResourceNotFoundException;
import ru.practicum.service.request.dto.RequestDto;
import ru.practicum.service.request.dto.RequestMapper;
import ru.practicum.service.request.model.Request;
import ru.practicum.service.request.model.RequestStatus;
import ru.practicum.service.request.repository.RequestRepository;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceDbImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final EventService eventService;

    private final UserService userService;

    @Override
    public List<RequestDto> getUserRequest(Integer userId, Integer eventId) {
        List<Request> requests = requestRepository.getUserEventRequests(userId, eventId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getUserRequests(Integer userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmRequest(Integer userId, Integer eventId, Integer reqId) {
        Request request = requestRepository.getUserRequest(userId, eventId, reqId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Запроса с id = %s не существует", reqId)));
        Event event = request.getEvent();

        if (event.getParticipantLimit().equals(0) || !event.getRequestModeration()) {
            throw new ForbiddenException(
                    "Попытка подтверждения заявки на участие для событич без ограничения количества участников " +
                            "или предварительной модерации");
        }

        if (event.getParticipantLimit() <= getConfirmedRequests(eventId)) {
            throw new ForbiddenException(
                    String.format("Для события с id = %s достигнут предел участников", eventId));
        }

        requestRepository.confirmRequest(reqId);
        request.setStatus(RequestStatus.CONFIRMED);

        if (event.getParticipantLimit().equals(getConfirmedRequests(eventId))) {
            requestRepository.rejectNotConfirmedRequest(eventId);
        }

        return RequestMapper.toRequestDto(request);
    }

    @Override
    public RequestDto cancelRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.getUserRequestById(userId, requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Запроса с id = %s не существует", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public RequestDto rejectRequest(Integer userId, Integer eventId, Integer reqId) {
        Request request = requestRepository.getUserRequest(userId, eventId, reqId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Запроса с id = %s не существует", reqId)));

        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public RequestDto addRequest(Integer userId, Integer eventId) {

        if (requestRepository.getUserRequest(userId, eventId).isPresent()) {
            throw new ForbiddenException(
                    String.format("Запрос с id=%s от пользователя с id=%s уже существует", userId, eventId));
        }
        Event event = eventService.getEventById(eventId);

        if (userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException(
                    String.format("Пользователь с id=%s является инициатором события с id=%s", userId, eventId));
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(
                    String.format("Событие с id=%s еще не было опубликовано", eventId));
        }

        if (!event.getParticipantLimit().equals(0) &&
                event.getParticipantLimit() <= getConfirmedRequests(eventId)) {
            throw new ForbiddenException(
                    String.format("Для события с id = %s достигнут предел участников", eventId));
        }

        User user = userService.getUserById(userId);
        Request request = new Request(null, event, LocalDateTime.now(), user, null);
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        request = requestRepository.save(request);
        log.info("Добавление нового пользователя c id {}", request.getId());
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public Integer getConfirmedRequests(Integer eventId) {
        return requestRepository.getConfirmedRequests(eventId);
    }
}
