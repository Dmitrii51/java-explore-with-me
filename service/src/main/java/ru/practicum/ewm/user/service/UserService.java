package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserNewDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {

    List<UserDto> getUsersList(List<Integer> ids, Integer from, Integer size);

    UserDto addUser(UserNewDto userNewDto);

    void deleteUser(Integer userId);

    User getUserById(Integer userId);
}
