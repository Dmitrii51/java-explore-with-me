package ru.practicum.service.event.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.event.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
