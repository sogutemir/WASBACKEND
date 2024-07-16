package com.codefusion.wasbackend.Category.mapper;

import com.codefusion.wasbackend.Category.dto.CategoryDto;
import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mapping(source = "store.id", target = "storeId")
    CategoryDto toDto(CategoryEntity categoryEntity);

    @Mapping(target = "store", ignore = true) // Ignore the store mapping here
    CategoryEntity toEntity(CategoryDto categoryDto);

    @Named("mapProducts")
    default List<CategoryDto.ProductEntityDto> mapProducts(List<ProductEntity> products) {
        return products != null ? products.stream().map(this::mapProduct).collect(Collectors.toList()) : null;
    }

    @Named("mapPrototypes")
    default List<CategoryDto.CategoryPrototypeEntityDto> mapPrototypes(List<CategoryPrototypeEntity> prototypes) {
        return prototypes != null ? prototypes.stream().map(this::mapPrototype).collect(Collectors.toList()) : null;
    }

    CategoryDto.ProductEntityDto mapProduct(ProductEntity product);

    CategoryDto.CategoryPrototypeEntityDto mapPrototype(CategoryPrototypeEntity prototype);

    @AfterMapping
    default void linkStore(@MappingTarget CategoryEntity categoryEntity, CategoryDto categoryDto) {
        if (categoryDto.getStoreId() != null) {
            StoreEntity store = new StoreEntity();
            store.setId(categoryDto.getStoreId());
            categoryEntity.setStore(store);
        }
    }

    @AfterMapping
    default void linkPrototypes(@MappingTarget CategoryEntity categoryEntity) {
        if (categoryEntity.getPrototypes() != null) {
            categoryEntity.getPrototypes().forEach(prototype -> prototype.setCategory(categoryEntity));
        }
    }

    @AfterMapping
    default void linkProducts(@MappingTarget CategoryEntity categoryEntity) {
        if (categoryEntity.getProducts() != null) {
            categoryEntity.getProducts().forEach(product -> product.setCategory(categoryEntity));
        }
    }
}
