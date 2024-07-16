package com.codefusion.wasbackend.transaction.mapper;

import com.codefusion.wasbackend.transaction.dto.ReturnTransactionDTO;
import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    TransactionEntity toEntity(TransactionDTO transactionDTO);

    @Mapping(target = "resourceFileId", source = "resourceFile.id")
    TransactionDTO toDto(TransactionEntity transactionEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionEntity partialUpdate(TransactionDTO transactionDTO, @MappingTarget TransactionEntity transactionEntity);

    TransactionEntity toEntity(ReturnTransactionDTO returnTransactionDTO);

    @Mapping(target = "resourceFile", source = "resourceFile")
    ReturnTransactionDTO toReturnDto(TransactionEntity transactionEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransactionEntity partialUpdate(ReturnTransactionDTO returnTransactionDTO, @MappingTarget TransactionEntity transactionEntity);
}