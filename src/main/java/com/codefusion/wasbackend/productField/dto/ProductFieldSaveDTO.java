package com.codefusion.wasbackend.productField.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductFieldSaveDTO implements Serializable {
    Long id;
    @NotBlank(message = "Name cannot be null")
    String name;
    @NotBlank(message = "Feature cannot be null")
    String feature;
}
