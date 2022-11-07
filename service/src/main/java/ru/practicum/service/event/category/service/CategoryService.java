package ru.practicum.service.event.category.service;

import ru.practicum.service.event.category.dto.CategoryDto;
import ru.practicum.service.event.category.dto.NewCategoryDto;
import ru.practicum.service.event.category.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Integer catId);

    List<CategoryDto> getCategoriesList(Integer from, Integer size);

    CategoryDto getCategory(Integer catId);

    Category getCatById(Integer catId);
}
