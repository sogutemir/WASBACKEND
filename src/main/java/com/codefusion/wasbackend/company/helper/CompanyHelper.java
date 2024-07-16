package com.codefusion.wasbackend.company.helper;

import com.codefusion.wasbackend.company.dto.CompanyEntityDto;
import com.codefusion.wasbackend.company.dto.CreateCompanyDto;
import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;

import java.util.stream.Collectors;

public class CompanyHelper {

    public static CompanyEntityDto convertToCompanyDTO(CompanyEntity companyEntity, CompanyEntityDto.ResourceFileEntityDto resourceFileDto) {
        return CompanyEntityDto.builder()
                .id(companyEntity.getId())
                .name(companyEntity.getName())
                .description(companyEntity.getDescription())
                .taxLevel(companyEntity.getTaxLevel())
                .isDeleted(companyEntity.getIsDeleted())
                .resourceFile(resourceFileDto)
                .stores(companyEntity.getStores().stream()
                        .map(store -> CompanyEntityDto.StoreEntityDto.builder()
                                .id(store.getId())
                                .name(store.getName())
                                .description(store.getDescription())
                                .address(store.getAddress())
                                .storePhoneNo(store.getStorePhoneNo())
                                .isDeleted(store.getIsDeleted())
                                .build())
                        .collect(Collectors.toList()))
                .user(CompanyEntityDto.UserEntityDto.builder()
                        .id(companyEntity.getUser().getId())
                        .name(companyEntity.getUser().getName())
                        .surname(companyEntity.getUser().getSurname())
                        .email(companyEntity.getUser().getEmail())
                        .phoneNo(companyEntity.getUser().getPhoneNo())
                        .telegramId(companyEntity.getUser().getTelegramId())
                        .isTelegram(companyEntity.getUser().getIsTelegram())
                        .activationRequestCode(companyEntity.getUser().getActivationRequestCode())
                        .telegramLinkTime(companyEntity.getUser().getTelegramLinkTime())
                        .ownerId(companyEntity.getUser().getOwnerId())
                        .isDeleted(companyEntity.getUser().getIsDeleted())
                        .build())
                .build();
    }

    public static CompanyEntityDto.ResourceFileEntityDto mapResourceFile(ResourceFileDTO fileDTO) {
        return CompanyEntityDto.ResourceFileEntityDto.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getFileName())
                .type(fileDTO.getContentType())
                .data(fileDTO.getData())
                .build();
    }

    public static CreateCompanyDto.ResourceFileEntityDto forCreatermapResourceFile(ResourceFileDTO fileDTO) {
        return CreateCompanyDto.ResourceFileEntityDto.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getFileName())
                .type(fileDTO.getContentType())
                .data(fileDTO.getData())
                .build();
    }

    public static CreateCompanyDto forCreateconvertToCompanyDTO(CompanyEntity companyEntity, CreateCompanyDto.ResourceFileEntityDto resourceFileDto) {
        return CreateCompanyDto.builder()
                .id(companyEntity.getId())
                .name(companyEntity.getName())
                .description(companyEntity.getDescription())
                .taxLevel(companyEntity.getTaxLevel())
                .isDeleted(companyEntity.getIsDeleted())
                .resourceFile(resourceFileDto)
                .build();
    }
}
