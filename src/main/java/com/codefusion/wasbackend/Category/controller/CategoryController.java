package com.codefusion.wasbackend.Category.controller;

import com.codefusion.wasbackend.Category.dto.CategoryAndPrototypesDto;
import com.codefusion.wasbackend.Category.dto.CategoryDto;
import com.codefusion.wasbackend.Category.dto.CategoryProfitDTO;
import com.codefusion.wasbackend.Category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getByCategoryId(id));
    }

    @GetMapping("/top5CategoriesByProfit/{storeId}")
    public ResponseEntity<List<CategoryProfitDTO>> getTop5CategoriesByProfit(@PathVariable Long storeId) {
        return ResponseEntity.ok(categoryService.getTop5CategoriesByProfit(storeId));
    }

    @GetMapping("/store/{storeId}/categories")
    public ResponseEntity<List<CategoryDto>> getCategoriesByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(categoryService.getCategoriesByStoreId(storeId));
    }


    @PostMapping("/add")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryAndPrototypesDto inputDto) {
        CategoryDto createdCategory = categoryService.addCategory(inputDto.getCategory(), inputDto.getPrototypes());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        try {
            categoryService.deleteByCategoryId(id);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        }
    }

}


