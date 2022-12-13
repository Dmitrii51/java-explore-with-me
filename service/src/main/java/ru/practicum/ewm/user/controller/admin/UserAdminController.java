package ru.practicum.ewm.user.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserNewDto;
import ru.practicum.ewm.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsersList(
            @RequestParam(required = false) List<Integer> ids,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(0) Integer size,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на получение информации о пользователях",
                request.getRemoteAddr(), request.getRequestURI());
        return userService.getUsersList(ids, from, size);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserNewDto userNewDto, HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на добавление нового пользователя",
                request.getRemoteAddr(), request.getRequestURI());
        return userService.addUser(userNewDto);
    }


    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId, HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на удаление пользователя c id {}",
                request.getRemoteAddr(), request.getRequestURI(), userId);
        userService.deleteUser(userId);
    }
}
