package ru.practicum.ewm.event.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.category.dto.CategoryDto;
import ru.practicum.ewm.event.category.dto.CategoryMapper;
import ru.practicum.ewm.event.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.category.model.Category;
import ru.practicum.ewm.event.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.ResourceNotFoundException;
import ru.practicum.ewm.util.PageBuilder;

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
        Pageable page = PageBuilder.getPage(from, size, "id", Sort.Direction.ASC);
        List<Category> categories = categoryRepository.findAll(page).toList();
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
    public Category getCategoryById(Integer catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) {
            log.warn("Попытка изменения информации о несуществующей категории с id - {}", catId);
            throw new ResourceNotFoundException(String.format("Категории с id = %s не существует", catId));
        }
        return category.get();
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        try {
            getCategoryById(categoryDto.getId());
            Category category = categoryRepository.save(CategoryMapper.fromCategoryDto(categoryDto));
            log.info("Изменение информации о категории с id {}", category.getId());
            return CategoryMapper.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Название категории должно быть уникальными");
        }
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        try {
            Category newCategory = categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto));
            log.info("Добавление новой категории c id {}", newCategory.getId());
            return CategoryMapper.toCategoryDto(newCategory);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Название категории должно быть уникальными");
        }
    }

    @Override
    public void deleteCategory(Integer catId) {
        getCategoryById(catId);
        categoryRepository.deleteById(catId);
        log.info("Удаление категории с id {}", catId);
    }
}
