package ru.practicum.ewm.event.category.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.category.dto.CategoryDto;
import ru.practicum.ewm.event.category.service.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    List<CategoryDto> getCategoriesList(
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(0) Integer size) {
        return categoryService.getCategoriesList(from, size);
    }

    @GetMapping("/{catId}")
    CategoryDto getCategory(@PathVariable @Min(0) Integer catId) {
        return categoryService.getCategory(catId);
    }
}
