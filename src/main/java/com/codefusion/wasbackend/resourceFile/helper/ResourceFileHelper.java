package com.codefusion.wasbackend.resourceFile.helper;

import com.codefusion.wasbackend.product.dto.ReturnProductDTO;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;

public class ResourceFileHelper {

    public static ReturnProductDTO.ResourceFileDto mapResourceFile(ResourceFileDTO fileDTO) {
        return ReturnProductDTO.ResourceFileDto.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getFileName())
                .type(fileDTO.getContentType())
                .data(fileDTO.getData())
                .build();
    }
}