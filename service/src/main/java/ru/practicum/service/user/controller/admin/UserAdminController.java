package ru.practicum.service.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.user.dto.UserDto;
import ru.practicum.service.user.dto.UserNewDto;
import ru.practicum.service.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsersList(
            @RequestParam(required = false) List<Integer> ids,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(0) Integer size) {
        return userService.getUsersList(ids, from, size);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserNewDto userNewDto) {
        return userService.addUser(userNewDto);
    }


    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }
}
