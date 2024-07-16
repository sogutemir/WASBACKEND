package com.codefusion.wasbackend.Category.dto;

import com.codefusion.wasbackend.CategoryPrototype.dto.CategoryPrototypeDto;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CategoryAndPrototypesDto {
    CategoryDto category;
    List<CategoryPrototypeDto> prototypes;
}
