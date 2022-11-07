package ru.practicum.service.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.request.dto.RequestDto;
import ru.practicum.service.request.service.RequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<RequestDto> getUserRequest(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer eventId) {
        return requestService.getUserRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    RequestDto confirmRequest(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer eventId,
            @PathVariable @Min(0) Integer reqId) {
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    RequestDto rejectRequest(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer eventId,
            @PathVariable @Min(0) Integer reqId) {
        return requestService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    List<RequestDto> getUserRequests(@PathVariable @Min(0) Integer userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    RequestDto addRequest(
            @PathVariable @Min(0) Integer userId,
            @RequestParam @Min(0) Integer eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    RequestDto cancelRequest(
            @PathVariable @Min(0) Integer userId,
            @PathVariable @Min(0) Integer requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
