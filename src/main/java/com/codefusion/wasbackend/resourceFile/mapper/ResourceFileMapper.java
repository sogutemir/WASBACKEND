package com.codefusion.wasbackend.resourceFile.mapper;

import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.model.ResourceFileEntity;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResourceFileMapper {

    @Mapping(target = "data", ignore = true)
    ReturnStoreDTO.ResourceFileDto toDto(ResourceFileEntity entity);

    @Mapping(source = "fileName", target = "name")
    @Mapping(source = "contentType", target = "type")
    @Mapping(source = "data", target = "data")
    ReturnStoreDTO.ResourceFileDto toReturnDto(ResourceFileDTO resourceFileDTO);

    @Mapping(target = "data", ignore = true)
    ResourceFileEntity toEntity(ReturnStoreDTO.ResourceFileDto dto);
}
