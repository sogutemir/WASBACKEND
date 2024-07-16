package com.codefusion.wasbackend.company.mapper;

import com.codefusion.wasbackend.company.dto.CompanyEntityDto;
import com.codefusion.wasbackend.company.dto.CreateCompanyDto;
import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyEntityMapper {
    CompanyEntity toEntity(CompanyEntityDto companyEntityDto);

    CompanyEntityDto toDto(CompanyEntity companyEntity);

    @AfterMapping
    default void linkStores(@MappingTarget CompanyEntity companyEntity) {
        if (companyEntity.getStores() != null) {
            companyEntity.getStores().forEach(store -> store.setCompany(companyEntity));
        }
    }

    @AfterMapping
    default void linkUser(@MappingTarget CompanyEntity companyEntity) {
        UserEntity user = companyEntity.getUser();
        if (user != null) {
            user.setCompany(companyEntity);
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CompanyEntity partialUpdate(CompanyEntityDto companyEntityDto, @MappingTarget CompanyEntity companyEntity);


}
