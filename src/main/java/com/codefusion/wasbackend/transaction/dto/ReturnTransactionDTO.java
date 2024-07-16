package com.codefusion.wasbackend.transaction.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.codefusion.wasbackend.transaction.model.TransactionEntity}
 */
@Data
@Builder
public class ReturnTransactionDTO implements Serializable {
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
    ProductDTO product;
    ResourceFileDto resourceFile;


    /**
     * DTO for {@link com.codefusion.wasbackend.product.model.ProductEntity}
     */
    @Data
    @Builder
    public static class ProductDTO implements Serializable {
        Long id;
        Boolean isDeleted;
        @NotBlank(message = "Name cannot be null")
        String name;
        @NotBlank(message = "Model cannot be null")
        String model;
        int currentStock;
        double profit;
        @NotEmpty(message = "Product code cannot be null")
        String productCode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ResourceFileDto implements Serializable {
        Long id;
        String name;
        String type;
        byte[] data;
        LocalDateTime uploadDate;
    }
}