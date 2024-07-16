package com.codefusion.wasbackend.CategoryPrototype.mapper;

import com.codefusion.wasbackend.CategoryPrototype.dto.CategoryPrototypeDto;
import com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryPrototypeMapper {
    CategoryPrototypeEntity toEntity(CategoryPrototypeDto categoryPrototypeDto);

    CategoryPrototypeDto toDto(CategoryPrototypeEntity categoryPrototypeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoryPrototypeEntity partialUpdate(CategoryPrototypeDto categoryPrototypeDto, @MappingTarget CategoryPrototypeEntity categoryPrototypeEntity);
}