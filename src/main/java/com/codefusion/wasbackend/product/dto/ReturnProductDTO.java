package com.codefusion.wasbackend.product.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.codefusion.wasbackend.product.model.ProductEntity}
 */
@Builder
@Value
public class ReturnProductDTO implements Serializable {
    Long id;
    Boolean isDeleted;
    ResourceFileDto resourceFile;
    @NotBlank(message = "Name cannot be null")
    String name;
    @NotBlank(message = "Model cannot be null")
    String model;
    int currentStock;
    double profit;
    @NotEmpty(message = "Product code cannot be null")
    String productCode;
    StoreDto store;
    CategoryDto category;
    List<ProductFieldDto> productFields;
    List<TransactionDto> transactions;

    /**
     * DTO for {@link com.codefusion.wasbackend.resourceFile.model.ResourceFileEntity}
     */
    @Value
    @Builder
    public static class ResourceFileDto implements Serializable {
        Long id;
        String name;
        String type;
        byte[] data;
        LocalDateTime uploadDate;
    }

    /**
     * DTO for {@link com.codefusion.wasbackend.store.model.StoreEntity}
     */
    @Value
    @Builder
    public static class StoreDto implements Serializable {
        Long id;
        Boolean isDeleted;
        @NotBlank(message = "Name cannot be empty")
        String name;
        String description;
        @NotBlank(message = "Address cannot be empty")
        String address;
        String storePhoneNo;
    }

    /**
     * DTO for {@link com.codefusion.wasbackend.Category.model.CategoryEntity}
     */
    @Value
    @Builder
    public static class CategoryDto implements Serializable {
        Long id;
        String name;
        Boolean isDelete;
    }

    /**
     * DTO for {@link com.codefusion.wasbackend.productField.model.ProductFieldEntity}
     */
    @Value
    @Builder
    public static class ProductFieldDto implements Serializable {
        Long id;
        @NotBlank(message = "Name cannot be null")
        String name;
        Boolean isDeleted;
        @NotBlank(message = "Feature cannot be null")
        String feature;
    }

    /**
     * DTO for {@link com.codefusion.wasbackend.transaction.model.TransactionEntity}
     */
    @Value
    @Builder
    public static class TransactionDto implements Serializable {
        Long id;
        Boolean isDeleted;
        Boolean isBuying;
        @PastOrPresent(message = "Transaction date must be today or in the past")
        LocalDate date;
        @NotNull(message = "Price cannot be empty")
        Double price;
        @NotBlank(message = "Full name cannot be empty")
        String fullName;
        int quantity;
        @NotBlank(message = "Address cannot be empty")
        String address;
        @Size(min = 9, max = 13)
        @NotBlank(message = "Phone cannot be empty")
        String phone;
    }
}