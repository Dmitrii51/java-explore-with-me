package ru.practicum.service.event.category.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.event.category.dto.CategoryDto;
import ru.practicum.service.event.category.dto.NewCategoryDto;
import ru.practicum.service.event.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PatchMapping
    CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping
    CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    void deleteCategory(@PathVariable @Min(0) Integer catId) {
        categoryService.deleteCategory(catId);
    }
}
