package com.codefusion.wasbackend.store.service;

import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.company.repository.CompanyRepository;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.resourceFile.mapper.ResourceFileMapper;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.dto.StoreProfitDTO;
import com.codefusion.wasbackend.store.helper.StoreHelper;
import com.codefusion.wasbackend.store.mapper.StoreMapper;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StoreService {

    private final StoreRepository repository;
    private final ResourceFileService resourceFileService;
    private final StoreMapper storeMapper;
    private final UserRepository userRepository;
    private final ResourceFileMapper resourceFileMapper;
    private final CompanyRepository companyRepository;

    public StoreService(StoreRepository repository, UserRepository userRepository, ResourceFileService resourceFileService,
                        StoreMapper storeMapper, ResourceFileMapper resourceFileMapper, CompanyRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.resourceFileService = resourceFileService;
        this.resourceFileMapper = resourceFileMapper;
        this.storeMapper = storeMapper;
        this.userRepository = userRepository;
    }



    /**
     * Retrieves a store by its ID.
     *
     * @param storeId the ID of the store to retrieve
     * @return the ReturnStoreDTO object representing the store
     * @throws RuntimeException if the store is not found
     */
    @Transactional(readOnly = true)
    public ReturnStoreDTO getStoreById(Long storeId) {
        StoreEntity storeEntity = repository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
        return StoreHelper.convertToReturnStoreDto(storeEntity, resourceFileService, resourceFileMapper, storeMapper);
    }

    /**
     * Retrieves a StoreEntity object by its ID.
     *
     * @param storeId the ID of the store
     * @return the StoreEntity object representing the retrieved store
     * @throws RuntimeException if the store is not found
     */
    @Transactional(readOnly = true)
    public StoreEntity getStoreEntityById(Long storeId) {
        return repository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
    }

    /**
     * Retrieves a list of all stores.
     *
     * @return a list of StoreDTO representing all stores
     */
    @Transactional(readOnly = true)
    public List<ReturnStoreDTO> getAllStores() {
        List<StoreEntity> storeEntities = repository.findAllByIsDeletedFalse();
        return StoreHelper.convertToReturnStoreDtoList(storeEntities, resourceFileService, resourceFileMapper, storeMapper);
    }

    @Transactional(readOnly = true)
    public List<StoreProfitDTO> getTop3StoresByProfitForUser(Long userId) {
        List<StoreEntity> storeEntities = repository.findByUserIdAndIsDeletedFalse(userId);

        return storeEntities.stream()
                .map(storeEntity -> {
                    double totalProfit = storeEntity.getProducts().stream()
                            .mapToDouble(ProductEntity::getProfit)
                            .sum();
                    return StoreProfitDTO.builder()
                            .name(storeEntity.getName())
                            .totalProfit(totalProfit)
                            .build();
                })
                .sorted(Comparator.comparingDouble(StoreProfitDTO::getTotalProfit).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReturnStoreDTO.ProductDto> getTop5MostProfitableProducts(Long storeId, boolean top) {
        StoreEntity storeEntity = repository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Stream<ProductEntity> sortedStream = storeEntity.getProducts().stream()
                .sorted(Comparator.comparingDouble(ProductEntity::getProfit));

        if (top) {
            sortedStream = sortedStream.sorted(Comparator.comparingDouble(ProductEntity::getProfit).reversed());
        }

        return sortedStream
                .limit(5)
                .map(StoreHelper::convertToProductDto)
                .collect(Collectors.toList());
    }




    /**
     * Retrieves a list of stores based on the user ID.
     *
     * @return a list of ReturnStoreDTO objects representing the stores associated with the specified user ID
     */
    @Transactional(readOnly = true)
    public List<ReturnStoreDTO> getStoresByUserId(Long userId) {
        List<StoreEntity> storeEntities = repository.findByUserIdAndIsDeletedFalse(userId);
        return StoreHelper.convertToReturnStoreDtoList(storeEntities, resourceFileService, resourceFileMapper, storeMapper);
    }

    @Transactional
    public StoreDTO addStore(StoreDTO dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(dto, "DTO cannot be null");

        StoreEntity storeEntity = instantiateStoreEntity(dto);
        storeEntity.setIsDeleted(false);

        CompanyEntity company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + dto.getCompanyId()));

        storeEntity.setCompany(company);

        if (company.getStores() == null) {
            company.setStores(new ArrayList<>());
        }
        company.getStores().add(storeEntity);

        if (file != null && !file.isEmpty()) {
            resourceFileService.handleFile(storeEntity, file, ResourceFileService.ProcessType.ADD);
        }

        repository.save(storeEntity);

        return storeMapper.toDto(storeEntity);
    }




    @Transactional
    public StoreDTO update(Long entityId, StoreDTO dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(entityId, "Entity ID cannot be null");
        Objects.requireNonNull(dto, "DTO cannot be null");

        StoreEntity existingEntity = repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));
        storeMapper.partialUpdate(dto, existingEntity);
        resourceFileService.handleFile(existingEntity, file, ResourceFileService.ProcessType.UPDATE);
        repository.flush();
        StoreEntity updatedEntity = repository.save(existingEntity);
        return storeMapper.toDto(updatedEntity);
    }


    /**
     * Deletes a store with the specified store ID.
     *
     * @param storeId the ID of the store to delete
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the store ID is null
     */
    @Transactional
    public void delete(Long storeId) throws IOException {
        Objects.requireNonNull(storeId, "Entity ID cannot be null");

        StoreEntity existingEntity = repository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + storeId));

        resourceFileService.handleFile(existingEntity, null, ResourceFileService.ProcessType.DELETE);

        existingEntity.setIsDeleted(true);

        repository.save(existingEntity);
    }



    private StoreEntity instantiateStoreEntity(StoreDTO storeDTO) {
        StoreEntity storeEntity = storeMapper.toEntity(storeDTO);

        List<UserEntity> userList = storeDTO.getUserIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId)))
                .collect(Collectors.toList());

        storeEntity.setUser(userList);
        return storeEntity;
    }


}
