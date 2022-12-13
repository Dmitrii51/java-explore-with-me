package ru.practicum.ewm.event.category.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.category.dto.CategoryDto;
import ru.practicum.ewm.event.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PatchMapping
    CategoryDto updateCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на изменение категории событий",
                request.getRemoteAddr(), request.getRequestURI());
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping
    CategoryDto addCategory(
            @RequestBody @Valid NewCategoryDto newCategoryDto,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на добавление новой категории событий",
                request.getRemoteAddr(), request.getRequestURI());
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    void deleteCategory(
            @PathVariable @Min(0) Integer catId,
            HttpServletRequest request) {
        log.info("{}: запрос администратора к эндпоинту {} на удаление категории событий с id {}",
                request.getRemoteAddr(), request.getRequestURI(), catId);
        categoryService.deleteCategory(catId);
    }
}
