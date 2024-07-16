package com.codefusion.wasbackend.CategoryPrototype.controller;

import com.codefusion.wasbackend.CategoryPrototype.service.CategoryPrototypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codefusion.wasbackend.CategoryPrototype.dto.CategoryPrototypeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoryPrototype")
@RequiredArgsConstructor
public class CategoryPrototypeController {

    private final CategoryPrototypeService categoryPrototypeService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CategoryPrototypeDto>> getCategoryPrototypesByCategoryId(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryPrototypeService.getPrototypesByCategoryId(categoryId));
    }

}