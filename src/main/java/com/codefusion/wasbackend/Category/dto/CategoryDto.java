package com.codefusion.wasbackend.Category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.codefusion.wasbackend.Category.model.CategoryEntity}
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto implements Serializable {
    private Long id;
    private String name;
    private Long storeId;
    private List<ProductEntityDto> products;
    private List<CategoryPrototypeEntityDto> prototypes;

    /**
     * DTO for {@link com.codefusion.wasbackend.store.model.StoreEntity}
     */
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class StoreEntityDto implements Serializable {
        private Long id;
        private Boolean isDeleted;
        @NotBlank(message = "Name cannot be empty")
        private String name;
        private String description;
        @NotBlank(message = "Address cannot be empty")
        private String address;
        private String storePhoneNo;
    }

    /**
     * DTO for {@link com.codefusion.wasbackend.product.model.ProductEntity}
     */
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ProductEntityDto implements Serializable {
        private Long id;
        private Boolean isDeleted;
        @NotBlank(message = "Name cannot be null")
        private String name;
        @NotBlank(message = "Model cannot be null")
        private String model;
        private int currentStock;
        private double profit;
        @NotEmpty(message = "Product code cannot be null")
        private String productCode;
    }

    /**
     * DTO for {@link com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity}
     */
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class CategoryPrototypeEntityDto implements Serializable {
        private Long id;
        private String name;
        private Boolean isDelete;
    }
}
