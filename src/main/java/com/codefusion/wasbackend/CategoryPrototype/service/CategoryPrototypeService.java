package com.codefusion.wasbackend.CategoryPrototype.service;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.CategoryPrototype.dto.CategoryPrototypeDto;
import com.codefusion.wasbackend.CategoryPrototype.mapper.CategoryPrototypeMapper;
import com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity;
import com.codefusion.wasbackend.CategoryPrototype.repository.CategoryPrototypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryPrototypeService {

    private final CategoryPrototypeMapper categoryPrototypeMapper;
    private final CategoryPrototypeRepository categoryPrototypeRepository;

    @Transactional(readOnly = true)
    public List<CategoryPrototypeDto> getPrototypesByCategoryId(Long categoryId){
        return categoryPrototypeRepository.findByCategoryIdAndIsDeleteFalse(categoryId).stream()
                .map(categoryPrototypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addCategoryPrototypes(CategoryEntity categoryEntity, List<CategoryPrototypeDto> categoryPrototypeDtos) {
        for (CategoryPrototypeDto categoryPrototypeDto : categoryPrototypeDtos) {
            CategoryPrototypeEntity categoryPrototypeEntity = categoryPrototypeMapper.toEntity(categoryPrototypeDto);
            categoryPrototypeEntity.setCategory(categoryEntity);
            categoryPrototypeRepository.save(categoryPrototypeEntity);
        }
    }
}
