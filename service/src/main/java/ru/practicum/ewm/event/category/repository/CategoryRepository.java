package ru.practicum.ewm.event.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
