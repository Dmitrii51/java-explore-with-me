package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.ResourceNotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserNewDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceDbImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsersList(List<Integer> ids, Integer from, Integer size) {
        List<User> users = (ids != null) ? userRepository.findAllById(ids)
                : userRepository.findAll(PageRequest.of(from / size, size)).toList();
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(UserNewDto userNewDto) {
        try {
            User newUser = userRepository.save(UserMapper.fromUserNewDto(userNewDto));
            log.info("Добавление нового пользователя c id {}", newUser.getId());
            return UserMapper.toUserDto(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Имя пользователя и почтовый адрес должны быть уникальными");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("Попытка удаления несуществующего пользователя с id - {}", userId);
            throw new ResourceNotFoundException(String.format("Пользователя с id = %s не существует", userId));
        }
        userRepository.deleteById(userId);
        log.info("Удаление пользователя с id {}", userId);
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Пользователя с id = %s не существует", userId)));
    }
}
