package ru.practicum.service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.service.exception.ResourceNotFoundException;
import ru.practicum.service.user.dto.UserDto;
import ru.practicum.service.user.dto.UserMapper;
import ru.practicum.service.user.dto.UserNewDto;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceDbImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserNewDto userNewDto) {
        User newUser = userRepository.save(UserMapper.fromUserNewDto(userNewDto));
        log.info("Добавление нового пользователя c id {}", newUser.getId());
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public List<UserDto> getUsersList(List<Integer> ids, Integer from, Integer size) {
        List<User> users = (ids != null) ? userRepository.findAllById(ids)
                : userRepository.findAll(PageRequest.of(from / size, size)).toList();
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        validateUser(userId);
        userRepository.deleteById(userId);
        log.info("Удаление пользователя с id {}", userId);
    }

    private void validateUser(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("Попытка изменения информации о несуществующем пользователе с id - {}", userId);
            throw new ResourceNotFoundException(String.format("Пользователя с id = %s не существует", userId));
        }
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Пользователя с id = %s не существует", userId)));
    }
}
