package com.codefusion.wasbackend.product.helper;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.product.dto.ReturnProductDTO;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.helper.ResourceFileHelper;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;

import java.util.stream.Collectors;

public class ProductHelper {

    public static ReturnStoreDTO.ProductDto convertToProductDto(ProductEntity productEntity) {
        return ReturnStoreDTO.ProductDto.builder()
                .id(productEntity.getId())
                .isDeleted(productEntity.getIsDeleted())
                .name(productEntity.getName())
                .model(productEntity.getModel())
                .currentStock(productEntity.getCurrentStock())
                .profit(productEntity.getProfit())
                .productCode(productEntity.getProductCode())
                .build();
    }

    public static ReturnProductDTO convertToReturnProductDto(ProductEntity productEntity, ResourceFileDTO fileDTO) {
        ReturnProductDTO.ResourceFileDto resourceFileDto = null;
        if (fileDTO != null) {
            resourceFileDto = ResourceFileHelper.mapResourceFile(fileDTO);
        }

        return ReturnProductDTO.builder()
                .id(productEntity.getId())
                .isDeleted(productEntity.getIsDeleted())
                .resourceFile(resourceFileDto)
                .name(productEntity.getName())
                .model(productEntity.getModel())
                .currentStock(productEntity.getCurrentStock())
                .profit(productEntity.getProfit())
                .productCode(productEntity.getProductCode())
                .store(productEntity.getStore() != null ? mapStore(productEntity.getStore()) : null)
                .category(productEntity.getCategory() != null ? mapCategory(productEntity.getCategory()) : null)
                .productFields(productEntity.getProductFields().stream().map(ProductHelper::mapProductField).collect(Collectors.toList()))
                .transactions(productEntity.getTransactions().stream().map(ProductHelper::mapTransaction).collect(Collectors.toList()))
                .build();
    }

    private static ReturnProductDTO.StoreDto mapStore(StoreEntity storeEntity) {
        return ReturnProductDTO.StoreDto.builder()
                .id(storeEntity.getId())
                .isDeleted(storeEntity.getIsDeleted())
                .name(storeEntity.getName())
                .description(storeEntity.getDescription())
                .address(storeEntity.getAddress())
                .storePhoneNo(storeEntity.getStorePhoneNo())
                .build();
    }

    private static ReturnProductDTO.CategoryDto mapCategory(CategoryEntity categoryEntity) {
        return ReturnProductDTO.CategoryDto.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .build();
    }

    private static ReturnProductDTO.ProductFieldDto mapProductField(ProductFieldEntity productFieldEntity) {
        return ReturnProductDTO.ProductFieldDto.builder()
                .id(productFieldEntity.getId())
                .name(productFieldEntity.getName())
                .isDeleted(productFieldEntity.getIsDeleted())
                .feature(productFieldEntity.getFeature())
                .build();
    }

    private static ReturnProductDTO.TransactionDto mapTransaction(TransactionEntity transactionEntity) {
        return ReturnProductDTO.TransactionDto.builder()
                .id(transactionEntity.getId())
                .isDeleted(transactionEntity.getIsDeleted())
                .isBuying(transactionEntity.getIsBuying())
                .date(transactionEntity.getDate())
                .price(transactionEntity.getPrice())
                .fullName(transactionEntity.getFullName())
                .quantity(transactionEntity.getQuantity())
                .address(transactionEntity.getAddress())
                .phone(transactionEntity.getPhone())
                .build();
    }

}