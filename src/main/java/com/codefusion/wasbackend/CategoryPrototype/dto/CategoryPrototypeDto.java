package com.codefusion.wasbackend.CategoryPrototype.dto;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryPrototypeDto {
    Long id;
    String name;
    Boolean isDelete;
    CategoryEntity category;
}