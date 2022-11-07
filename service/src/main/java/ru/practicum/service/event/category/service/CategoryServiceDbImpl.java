package ru.practicum.service.event.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.service.event.category.dto.CategoryDto;
import ru.practicum.service.event.category.dto.CategoryMapper;
import ru.practicum.service.event.category.dto.NewCategoryDto;
import ru.practicum.service.event.category.model.Category;
import ru.practicum.service.event.category.repository.CategoryRepository;
import ru.practicum.service.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceDbImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategoriesList(Integer from, Integer size) {
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size)).toList();
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Категории с id = %s не существует", catId)));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public Category getCatById(Integer catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) {
            log.warn("Попытка изменения информации о несуществующей категории с id - {}", catId);
            throw new ResourceNotFoundException(String.format("Категории с id = %s не существует", catId));
        }
        return category.get();
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        getCatById(categoryDto.getId());
        Category category = categoryRepository.save(CategoryMapper.fromCategoryDto(categoryDto));
        log.info("Изменение информации о категории с id {}", category.getId());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto));
        log.info("Добавление новой категории c id {}", newCategory.getId());
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Override
    public void deleteCategory(Integer catId) {
        getCatById(catId);
        categoryRepository.deleteById(catId);
        log.info("Удаление категории с id {}", catId);
    }
}
