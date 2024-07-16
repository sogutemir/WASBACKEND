package com.codefusion.wasbackend.store.helper;

import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.mapper.ResourceFileMapper;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.mapper.StoreMapper;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class StoreHelper {

    private final StoreMapper storeMapper;
    private final ResourceFileMapper resourceFileMapper;
    private final ResourceFileService resourceFileService;
    private final UserRepository userRepository;

    public StoreHelper(StoreMapper storeMapper, ResourceFileMapper resourceFileMapper, ResourceFileService resourceFileService, UserRepository userRepository) {
        this.storeMapper = storeMapper;
        this.resourceFileMapper = resourceFileMapper;
        this.resourceFileService = resourceFileService;
        this.userRepository = userRepository;
    }

    public static ReturnStoreDTO convertToReturnStoreDto(StoreEntity storeEntity, ResourceFileService resourceFileService, ResourceFileMapper resourceFileMapper, StoreMapper storeMapper) {
        ReturnStoreDTO dto = storeMapper.toReturnDto(storeEntity);
        ReturnStoreDTO.ResourceFileDto resourceFileDto = null;
        if (storeEntity.getResourceFile() != null) {
            try {
                ResourceFileDTO fileDTO = resourceFileService.downloadFile(storeEntity.getResourceFile().getId());
                resourceFileDto = resourceFileMapper.toReturnDto(fileDTO);
            } catch (FileNotFoundException e) {
                // Log exception and continue
                e.printStackTrace();
            }
        }
        return ReturnStoreDTO.builder()
                .id(dto.getId())
                .isDeleted(dto.getIsDeleted())
                .name(dto.getName())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .storePhoneNo(dto.getStorePhoneNo())
                .user(dto.getUser())
                .products(dto.getProducts())
                .resourceFile(resourceFileDto)
                .build();
    }

    public static List<ReturnStoreDTO> convertToReturnStoreDtoList(List<StoreEntity> storeEntities, ResourceFileService resourceFileService, ResourceFileMapper resourceFileMapper, StoreMapper storeMapper) {
        return storeEntities.stream()
                .map(storeEntity -> convertToReturnStoreDto(storeEntity, resourceFileService, resourceFileMapper, storeMapper))
                .collect(Collectors.toList());
    }

    public static ReturnStoreDTO.ProductDto convertToProductDto(ProductEntity productEntity) {
        return ReturnStoreDTO.ProductDto.builder()
                .id(productEntity.getId())
                .isDeleted(productEntity.getIsDeleted())
                .name(productEntity.getName())
                .model(productEntity.getModel())
                .currentStock(productEntity.getCurrentStock())
                .profit(productEntity.getProfit())
                .productCode(productEntity.getProductCode())
                .build();
    }

}