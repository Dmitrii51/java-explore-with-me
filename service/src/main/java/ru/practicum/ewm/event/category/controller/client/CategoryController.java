package ru.practicum.ewm.event.category.controller.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.category.dto.CategoryDto;
import ru.practicum.ewm.event.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    List<CategoryDto> getCategoriesList(
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(0) Integer size,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение категорий событий",
                request.getRemoteAddr(), request.getRequestURI());
        return categoryService.getCategoriesList(from, size);
    }

    @GetMapping("/{catId}")
    CategoryDto getCategory(
            @PathVariable @Min(0) Integer catId,
            HttpServletRequest request) {
        log.info("{}: запрос к эндпоинту {} на получение категории событий с id {}",
                request.getRemoteAddr(), request.getRequestURI(), catId);
        return categoryService.getCategory(catId);
    }
}
