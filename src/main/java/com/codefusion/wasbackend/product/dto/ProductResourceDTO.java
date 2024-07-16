package com.codefusion.wasbackend.product.dto;

import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResourceDTO {

    private ProductDTO productDTO;
    private ResourceFileDTO resourceFileDTO;
}
