package ru.practicum.ewm.event.category.service;

import ru.practicum.ewm.event.category.dto.CategoryDto;
import ru.practicum.ewm.event.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.category.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Integer catId);

    List<CategoryDto> getCategoriesList(Integer from, Integer size);

    CategoryDto getCategory(Integer catId);

    Category getCategoryById(Integer catId);
}
