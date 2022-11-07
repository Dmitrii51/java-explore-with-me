package ru.practicum.service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
