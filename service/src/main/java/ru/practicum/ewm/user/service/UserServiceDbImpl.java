package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.ResourceNotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserNewDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.util.PageBuilder;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceDbImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsersList(List<Integer> ids, Integer from, Integer size) {
        Pageable page = PageBuilder.getPage(from, size, "id", Sort.Direction.ASC);
        List<User> users = (ids != null) ? userRepository.findAllById(ids)
                : userRepository.findAll(page).toList();
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteUser(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
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
