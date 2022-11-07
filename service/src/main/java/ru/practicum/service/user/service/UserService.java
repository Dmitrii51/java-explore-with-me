package ru.practicum.service.user.service;

import ru.practicum.service.user.dto.UserDto;
import ru.practicum.service.user.dto.UserNewDto;
import ru.practicum.service.user.model.User;

import java.util.List;

public interface UserService {

    UserDto addUser(UserNewDto userNewDto);

    List<UserDto> getUsersList(List<Integer> ids, Integer from, Integer size);

    void deleteUser(Integer userId);

    User getUserById(Integer userId);
}
