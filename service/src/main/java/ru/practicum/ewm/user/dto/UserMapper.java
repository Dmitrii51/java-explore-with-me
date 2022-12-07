package ru.practicum.ewm.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.model.User;

@UtilityClass
public class UserMapper {

    public User fromUserNewDto(UserNewDto userNewDto) {
        return new User(
                null,
                userNewDto.getName(),
                userNewDto.getEmail()
        );
    }

    public UserShortDto toUserDtoShort(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
