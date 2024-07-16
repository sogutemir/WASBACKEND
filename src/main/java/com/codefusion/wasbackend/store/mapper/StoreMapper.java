package com.codefusion.wasbackend.store.mapper;

import com.codefusion.wasbackend.resourceFile.mapper.ResourceFileMapper;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ResourceFileMapper.class})
public interface StoreMapper {

    @AfterMapping
    default void linkProducts(@MappingTarget StoreEntity storeEntity) {
        if (storeEntity.getProducts() != null) {
            storeEntity.getProducts().forEach(product -> product.setStore(storeEntity));
        }
    }

    @Mapping(target = "user", source = "userIds", qualifiedByName = "userEntitiesFromIds")
    StoreEntity toEntity(StoreDTO storeDTO);

    @Mapping(target = "resourceFileId", source = "resourceFile.id")
    @Mapping(target = "userIds", source = "user", qualifiedByName = "userIdsFromEntities")
    @Mapping(target = "companyId", source = "company.id")
    StoreDTO toDto(StoreEntity storeEntity);

    @Mapping(target = "user", source = "userIds", qualifiedByName = "userEntitiesFromIds")
    @Mapping(target = "company.id", source = "companyId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    StoreEntity partialUpdate(StoreDTO storeDTO, @MappingTarget StoreEntity storeEntity);

    @Named("userEntitiesFromIds")
    default List<UserEntity> userEntitiesFromIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }

        return userIds.stream()
                .map(id -> {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(id);
                    return userEntity;
                }).collect(Collectors.toList());
    }

    @Named("userIdsFromEntities")
    default List<Long> userIdsFromEntities(List<UserEntity> userEntities) {
        if (userEntities == null || userEntities.isEmpty()) {
            return null;
        }

        return userEntities.stream().map(UserEntity::getId).collect(Collectors.toList());
    }

    @Mapping(target = "resourceFile", source = "resourceFile")
    ReturnStoreDTO toReturnDto(StoreEntity storeEntity);

    StoreEntity toEntity(ReturnStoreDTO returnStoreDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    StoreEntity partialUpdate(ReturnStoreDTO returnStoreDTO, @MappingTarget StoreEntity storeEntity);
}
