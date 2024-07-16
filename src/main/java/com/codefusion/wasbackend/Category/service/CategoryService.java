package com.codefusion.wasbackend.Category.service;

import com.codefusion.wasbackend.Category.dto.CategoryDto;
import com.codefusion.wasbackend.Category.dto.CategoryProfitDTO;
import com.codefusion.wasbackend.Category.mapper.CategoryMapper;
import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.Category.repository.CategoryRepository;
import com.codefusion.wasbackend.CategoryPrototype.dto.CategoryPrototypeDto;
import com.codefusion.wasbackend.CategoryPrototype.service.CategoryPrototypeService;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryPrototypeService categoryPrototypeService;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        return categoryEntities.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getByCategoryId(Long id){
        try {
            CategoryEntity categoryEntity = categoryRepository.getById(id);
            return categoryMapper.toDto(categoryEntity);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with ID " + id + " not found", e);
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByStoreId(Long storeId) {
        List<CategoryEntity> categories = categoryRepository.findAllByStoreId(storeId);
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryProfitDTO> getTop5CategoriesByProfit(Long storeId) {
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByStoreId(storeId);

        return categoryEntities.stream()
                .map(category -> {
                    double totalProfit = category.getProducts().stream()
                            .mapToDouble(product -> product.getProfit())
                            .sum();
                    return CategoryProfitDTO.builder()
                            .categoryName(category.getName())
                            .totalProfit(totalProfit)
                            .build();
                })
                .sorted(Comparator.comparingDouble(CategoryProfitDTO::getTotalProfit).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto, List<CategoryPrototypeDto> categoryPrototypeDtos) {
        if (categoryDto.getStoreId() == null) {
            throw new IllegalArgumentException("Store ID cannot be null");
        }

        Optional<StoreEntity> storeEntityOptional = storeRepository.findById(categoryDto.getStoreId());
        if (!storeEntityOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid Store ID: " + categoryDto.getStoreId());
        }

        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDto);
        storeEntityOptional.ifPresent(categoryEntity::setStore);

        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);

        categoryPrototypeService.addCategoryPrototypes(savedCategoryEntity, categoryPrototypeDtos);

        return categoryMapper.toDto(savedCategoryEntity);
    }






    @Transactional
    public void deleteByCategoryId(Long id){
        try {
            CategoryEntity categoryEntity = categoryRepository.getById(id);
            categoryRepository.delete(categoryEntity);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with ID " + id + " not found", e);
        }
    }

}