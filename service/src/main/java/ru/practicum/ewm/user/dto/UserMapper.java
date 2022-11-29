package ru.practicum.ewm.user.dto;

import ru.practicum.ewm.user.model.User;

public class UserMapper {

    public static User fromUserNewDto(UserNewDto userNewDto) {
        return new User(
                null,
                userNewDto.getName(),
                userNewDto.getEmail()
        );
    }

    public static UserShortDto toUserDtoShort(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
