package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.ResourceNotFoundException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceDbImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final EventService eventService;

    private final UserService userService;

    @Override
    public List<RequestDto> getUserEventRequests(Integer userId, Integer eventId) {
        List<Request> requests = requestRepository.findUserEventRequests(userId, eventId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto confirmRequest(Integer userId, Integer eventId, Integer reqId) {
        Request request = requestRepository.findUserRequest(userId, eventId, reqId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Запроса с id = %s не существует", reqId)));
        Event event = request.getEvent();
        validateEventBeforeRequestConfirm(event, eventId);
        requestRepository.confirmRequest(reqId);
        request.setStatus(RequestStatus.CONFIRMED);
        if (event.getParticipantLimit().equals(getConfirmedRequests(eventId))) {
            requestRepository.rejectNotConfirmedRequest(eventId);
        }
        return RequestMapper.toRequestDto(request);
    }

    private void validateEventBeforeRequestConfirm(Event event, Integer eventId) {
        if (event.getParticipantLimit().equals(0) || !event.getRequestModeration()) {
            throw new ForbiddenException(
                    "Подтверждение заявки для данного события не требуется, " +
                            "так лимит заявок для события равен 0 или отключена пре-модерация заявок");
        }

        if (event.getParticipantLimit() <= getConfirmedRequests(eventId)) {
            throw new ForbiddenException(
                    "Нельзя подтвердить заявку, так как  лимит по заявкам на данное событие уже достигнут");
        }
    }

    @Override
    @Transactional
    public RequestDto rejectRequest(Integer userId, Integer eventId, Integer reqId) {
        Request request = requestRepository.findUserRequest(userId, eventId, reqId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Запроса с id = %s не существует", reqId)));
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getUserRequests(Integer userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto addRequest(Integer userId, Integer eventId) {
        Event event = eventService.getEventById(eventId);
        validateNewRequest(userId, eventId, event);
        User user = userService.getUserById(userId);
        Request request = new Request(null, event, LocalDateTime.now(), user, null);
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        request = requestRepository.save(request);
        log.info("Добавление нового запроса на участие c id {}", request.getId());
        return RequestMapper.toRequestDto(request);
    }

    private void validateNewRequest(Integer userId, Integer eventId, Event event) {

        if (requestRepository.findUserRequest(userId, eventId).isPresent()) {
            throw new ForbiddenException("Нельзя добавлять повторный запрос");
        }

        if (userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("Инициатор события не может добавлять запрос на участие в своём событии");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Нельзя участвовать в неопубликованном событии");
        }

        if (!event.getParticipantLimit().equals(0) &&
                event.getParticipantLimit() <= getConfirmedRequests(eventId)) {
            throw new ForbiddenException("У события достигнут лимит запросов на участие");
        }
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findUserRequestById(userId, requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Запроса с id = %s не существует", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public Integer getConfirmedRequests(Integer eventId) {
        return requestRepository.findConfirmedRequests(eventId);
    }
}
