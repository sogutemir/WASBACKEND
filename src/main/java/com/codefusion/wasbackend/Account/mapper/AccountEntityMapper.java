package com.codefusion.wasbackend.Account.mapper;

import com.codefusion.wasbackend.Account.dto.AccountEntityDto;
import com.codefusion.wasbackend.Account.model.AccountEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountEntityMapper {
    /**
     * Converts an AccountEntityDto object to an AccountEntity object.
     *
     * @param accountEntityDto the AccountEntityDto to be converted
     * @return the converted AccountEntity object
     */
    AccountEntity toEntity(AccountEntityDto accountEntityDto);

    /**
     * Converts an AccountEntity object to an AccountEntityDto object.
     *
     * @param accountEntity the AccountEntity object to be converted
     * @return the AccountEntityDto object representing the converted AccountEntity object
     */
    AccountEntityDto toDto(AccountEntity accountEntity);

    /**
     * Partially updates an AccountEntity by applying the non-null fields from the provided AccountEntityDto.
     *
     * @param accountEntityDto The DTO object containing the updated fields.
     * @param accountEntity    The target AccountEntity to be updated.
     * @return The updated AccountEntity object.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AccountEntity partialUpdate(AccountEntityDto accountEntityDto, @MappingTarget AccountEntity accountEntity);

}