package com.codefusion.wasbackend.productField.dto;

import com.codefusion.wasbackend.product.model.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.codefusion.wasbackend.productField.model.ProductFieldEntity}
 */
@Value
public class ProductFieldDTO implements Serializable {
    Long id;
    @NotBlank(message = "Name cannot be null")
    String name;
    @NotBlank(message = "Feature cannot be null")
    String feature;
    ProductEntity product;

}