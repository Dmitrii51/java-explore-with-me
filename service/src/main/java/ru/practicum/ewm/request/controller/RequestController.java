package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<RequestDto> getUserEventRequests(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer eventId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение информации" +
                        " о запросах на участие пользователя с id {} в событии с id {}",
                request.getRemoteAddr(), request.getRequestURI(), userId, eventId);
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    RequestDto confirmRequest(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer eventId,
            @PathVariable @Min(0) Integer reqId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на подтверждение чужой заявки на участие с id {}" +
                        " в событии с id {} пользователя с id {}",
                request.getRemoteAddr(), request.getRequestURI(), reqId, eventId, userId);
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    RequestDto rejectRequest(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer eventId,
            @PathVariable @Min(0) Integer reqId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на отклонение чужой заявки на участие с id {}" +
                        " в событии с id {} пользователя с id {}",
                request.getRemoteAddr(), request.getRequestURI(), reqId, eventId, userId);
        return requestService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    List<RequestDto> getUserRequests(
            @PathVariable @Min(0) Integer userId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение информации о заявках пользователя с id {}" +
                        " на участие в чужих событиях",
                request.getRemoteAddr(), request.getRequestURI(), userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    RequestDto addRequest(
            @PathVariable @Min(0) Integer userId,
            @RequestParam @Min(0) Integer eventId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на добавление запроса от пользователя с id {}" +
                        " на участие в событии с id {}",
                request.getRemoteAddr(), request.getRequestURI(), userId, eventId);
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    RequestDto cancelRequest(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer requestId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на отмену своего запроса с id {} пользователем с id {}",
                request.getRemoteAddr(), request.getRequestURI(), requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }
}
