package com.codefusion.wasbackend.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.codefusion.wasbackend.store.model.StoreEntity}
 */
@Data
@Builder
@AllArgsConstructor
public class ReturnStoreDTO implements Serializable {
    Long id;
    Boolean isDeleted;
    ResourceFileDto resourceFile;
    @NotBlank(message = "Name cannot be empty")
    String name;
    String description;
    @NotBlank(message = "Address cannot be empty")
    String address;
    String storePhoneNo;
    double totalProfit;
    List<UserDto> user;
    List<ProductDto> products;
    List<CategoryEntityDto> categories;

    /**
     * DTO for {@link com.codefusion.wasbackend.resourceFile.model.ResourceFileEntity}
     */
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

    /**
     * DTO for {@link com.codefusion.wasbackend.user.model.UserEntity}
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class UserDto implements Serializable {
        Long id;
        Boolean isDeleted;
        @NotBlank(message = "Name cannot be empty")
        String name;
        @NotBlank(message = "Surname cannot be empty")
        String surname;
        @NotBlank(message = "Email cannot be empty")
        String email;
        @NotBlank(message = "Phone cannot be empty")
        String phoneNo;
        Long telegramId;
        Boolean isTelegram;
        String ActivationRequestCode;
        Date telegramLinkTime;
    }

    /**
     * DTO for {@link com.codefusion.wasbackend.product.model.ProductEntity}
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class ProductDto implements Serializable {
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

    /**
     * DTO for {@link com.codefusion.wasbackend.Category.model.CategoryEntity}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryEntityDto implements Serializable {
        private Long id;
        private String name;
        private Boolean isDelete;
    }
}
