package com.codefusion.wasbackend.productField.service;

import com.codefusion.wasbackend.productField.dto.ProductFieldSaveDTO;
import com.codefusion.wasbackend.productField.dto.ProductFieldDTO;
import com.codefusion.wasbackend.productField.mapper.ProductFieldMapper;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.productField.repository.ProductFieldRepository;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.product.model.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductFieldService {

    private final ProductFieldMapper productFieldMapper;
    private final ProductFieldRepository repository;
    private final ProductRepository productRepository;

    /**
     * Retrieves the {@link ProductFieldDTO} associated with the given fieldId.
     *
     * @param fieldId the ID of the product field to retrieve
     * @return the {@link ProductFieldDTO} associated with the given fieldId
     * @throws IllegalArgumentException if no product field is found with the given fieldId
     */
    @Transactional(readOnly = true)
    public ProductFieldDTO getProductFieldById(Long fieldId) {
        ProductFieldEntity fieldEntity = repository.findById(fieldId).orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + fieldId));
        if (fieldEntity.getIsDeleted()) {
            throw new RuntimeException("The requested product field has been deleted");
        }
        return productFieldMapper.toDto(fieldEntity);
    }

    /**
     * Retrieves all product fields.
     *
     * @return a list of {@link ProductFieldDTO} representing all product fields.
     */
    @Transactional(readOnly = true)
    public List<ProductFieldDTO> getAllProductField() {
        List<ProductFieldEntity> productFieldEntities = repository.findAllByIsDeletedFalse();
        return productFieldEntities.stream()
                .map(productFieldMapper::toDto)
                .toList();
    }

    /**
     * Retrieves the list of {@link ProductFieldDTO} objects associated with a given productId.
     *
     * @param productId the ID of the product
     * @return the list of ProductFieldDTO objects associated with the given productId
     * @throws IllegalArgumentException if productId is null
     */
    @Transactional(readOnly = true)
    public List<ProductFieldDTO> getProductFieldByProductId(Long productId) {
        if(productId == null){
            throw new IllegalArgumentException("productId cannot be null");
        }
        List<ProductFieldEntity> productFieldEntities = repository.findByProductId(productId);
        return productFieldEntities.stream()
                .map(productFieldMapper::toDto)
                .toList();
    }

    /**
     * Adds a new product field.
     *
     * @param productFieldDTO the DTO representing the product field to be added (must not be null)
     * @return the DTO representing the added product field
     * @throws IllegalArgumentException if the productFieldDTO is null
     */
    @Transactional
    public ProductFieldDTO addProductField(ProductFieldDTO productFieldDTO){
        if(productFieldDTO == null){
            throw new IllegalArgumentException("ProductFieldDTO cannot be null");
        }
        try{
            ProductFieldEntity productFieldEntity = productFieldMapper.toEntity(productFieldDTO);
            ProductFieldEntity savedProductFieldEntity = repository.save(productFieldEntity);
            return productFieldMapper.toDto(savedProductFieldEntity);
        } catch(Exception e){
            throw e;
        }
    }

    /**
     * Adds a list of product fields to a specified product.
     *
     * @param productFieldSaveDTOs the list of {@link ProductFieldSaveDTO} representing the product fields to be added (must not be null)
     * @param productID the ID of the product to which the product fields will be added
     * @return the list of {@link ProductFieldDTO} representing the added product fields
     * @throws IllegalArgumentException if productFieldSaveDTOs is null
     */
    @Transactional
    public List<ProductFieldDTO> addProductFields(List<ProductFieldSaveDTO> productFieldSaveDTOs, Long productID){
        if(productFieldSaveDTOs == null){
            throw new IllegalArgumentException("ProductFieldSaveDTO cannot be null");
        }
        try{
            List<ProductFieldEntity> productFieldEntities = productFieldSaveDTOs.stream()
                    .map(productFieldSaveDTO -> {
                        ProductFieldEntity productFieldEntity = productFieldMapper.toEntity(productFieldSaveDTO);
                        ProductEntity product = productRepository.findById(productID).orElse(null);
                        productFieldEntity.setProduct(product);
                        return productFieldEntity;
                    }).collect(Collectors.toList());
            List<ProductFieldEntity> savedProductFieldEntities = repository.saveAll(productFieldEntities);
            return savedProductFieldEntities.stream()
                    .map(productFieldMapper::toDto)
                    .collect(Collectors.toList());
        } catch(Exception e){
            throw e;
        }
    }

    /**
     * Updates a product field with the specified ID.
     *
     * @param id The ID of the product field to update.
     * @param productFieldDTO The updated product field data.
     * @return The updated product field.
     * @throws IllegalArgumentException if the ID or product field DTO is null.
     */
    @Transactional
    public List<ProductFieldDTO> updateProductField(Long id, List<ProductFieldSaveDTO> productFieldDTO){
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (productFieldDTO == null) {
            throw new IllegalArgumentException("ProductFieldDTO cannot be null");
        }

        try {
            repository.deleteByProductId(id);
            return addProductFields(productFieldDTO, id);

        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Deletes a product field by its ID.
     *
     * @param fieldId the ID of the product field to be deleted
     * @throws IllegalArgumentException if the ID is null
     */
    @Transactional
    public void deleteProductField(Long fieldId) {
        try {
            ProductFieldEntity productField = repository.findById(fieldId).orElseThrow(() -> new RuntimeException("Product Field not found"));
            productField.setIsDeleted(true);
            repository.save(productField);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting product field", e);
        }
    }
}
