package com.codefusion.wasbackend.user.helper;

import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.Account.model.AccountEntity;

import java.util.stream.Collectors;

public class UserHelper {

    public static UserDTO convertToUserDTO(UserEntity userEntity, UserDTO.ResourceFileEntityDto resourceFileDto) {
        AccountEntity accountEntity = userEntity.getAccount();
        return UserDTO.builder()
                .id(userEntity.getId())
                .isDeleted(userEntity.getIsDeleted())
                .resourceFile(resourceFileDto)
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .email(userEntity.getEmail())
                .phoneNo(userEntity.getPhoneNo())
                .telegramId(userEntity.getTelegramId())
                .isTelegram(userEntity.getIsTelegram())
                .telegramLinkTime(userEntity.getTelegramLinkTime())
                .stores(userEntity.getStores().stream()
                        .map(UserHelper::mapStore)
                        .collect(Collectors.toList()))
                .roles(accountEntity != null ? accountEntity.getRoles() : null)
                .build();
    }

    public static UserDTO.StoreEntityDto mapStore(StoreEntity storeEntity) {
        return UserDTO.StoreEntityDto.builder()
                .id(storeEntity.getId())
                .isDeleted(storeEntity.getIsDeleted())
                .name(storeEntity.getName())
                .description(storeEntity.getDescription())
                .address(storeEntity.getAddress())
                .storePhoneNo(storeEntity.getStorePhoneNo())
                .build();
    }

    public static UserDTO.ResourceFileEntityDto mapResourceFile(ResourceFileDTO fileDTO) {
        return UserDTO.ResourceFileEntityDto.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getFileName())
                .type(fileDTO.getContentType())
                .data(fileDTO.getData())
                .build();
    }
}
