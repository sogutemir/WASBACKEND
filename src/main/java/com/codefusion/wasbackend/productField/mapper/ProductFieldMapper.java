package com.codefusion.wasbackend.productField.mapper;

import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.productField.dto.ProductFieldDTO;
import com.codefusion.wasbackend.productField.dto.ProductFieldSaveDTO;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class, ProductMapper.class})
public interface ProductFieldMapper {

    /**
     * Converts a ProductFieldDTO object to a ProductFieldEntity object.
     *
     * @param productFieldDTO the ProductFieldDTO object representing the product field data transfer object to be converted
     * @return the ProductFieldEntity object representing the converted product field entity
     */
    ProductFieldEntity toEntity(ProductFieldDTO productFieldDTO);

    /**
     * Converts a ProductFieldEntity object to a ProductFieldDTO object.
     *
     * @param productFieldEntity the ProductFieldEntity object to be converted
     * @return the ProductFieldDTO object representing the converted product field
     */
    ProductFieldDTO toDto(ProductFieldEntity productFieldEntity);

    /**
     * Updates the specified ProductFieldEntity object with the properties from the given ProductFieldDTO object.
     *
     * @param productFieldDTO The ProductFieldDTO object containing the updated field properties.
     * @param productFieldEntity The ProductFieldEntity object to be updated.
     * @return The updated ProductFieldEntity object.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductFieldEntity partialUpdate(ProductFieldDTO productFieldDTO, @MappingTarget ProductFieldEntity productFieldEntity);

    ProductFieldEntity toEntity(ProductFieldSaveDTO productFieldSaveDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductFieldEntity partialUpdate(ProductFieldSaveDTO productFieldSaveDTO, @MappingTarget ProductFieldEntity productFieldEntity);
}
