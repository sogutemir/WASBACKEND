package com.codefusion.wasbackend.product.service;


import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.Category.repository.CategoryRepository;
import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.model.NotificationLevel;
import com.codefusion.wasbackend.product.dto.ReturnProductDTO;
import com.codefusion.wasbackend.base.utils.ProcessUploadFileService;
import com.codefusion.wasbackend.notification.service.NotificationService;
import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.helper.ProductHelper;
import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Service
public class ProductService {


    private final ProductRepository repository;
    private final ProductMapper productMapper;
    private final ProcessUploadFileService processUploadFileService;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;
    private final UserMapper userMapper;
    private final ResourceFileService resourceFileService;


    public ProductService(ProductRepository repository,
                          ResourceFileService resourceFileService, ProductMapper productMapper,
                          ProcessUploadFileService processUploadFileService, NotificationService notificationService,
                          StoreRepository storeRepository,UserMapper userMapper, CategoryRepository categoryRepository) {
        this.productMapper = productMapper;
        this.resourceFileService = resourceFileService;
        this.repository = repository;
        this.processUploadFileService = processUploadFileService;
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.notificationService = notificationService;
        this.userMapper = userMapper;
    }



    /**
     * Retrieves a {@link ProductDTO} by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the {@link ProductDTO} corresponding to the ID
     * @throws RuntimeException if the product is not found
     */
    @Transactional(readOnly = true)
    public ReturnProductDTO getProductById(Long productId) {
        ProductEntity productEntity = repository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new RuntimeException("Product not found or is deleted"));

        ResourceFileDTO fileDTO = null;
        if (productEntity.getResourceFile() != null) {
            try {
                fileDTO = resourceFileService.downloadFile(productEntity.getResourceFile().getId());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ProductHelper.convertToReturnProductDto(productEntity, fileDTO);
    }

    /**
     * Retrieves a {@link ProductEntity} by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the {@link ProductEntity} corresponding to the ID
     * @throws RuntimeException if the product is not found or has been deleted
     */
    @Transactional(readOnly = true)
    public ProductEntity getProductEntityById(Long productId) {
        ProductEntity product = repository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if(product.getIsDeleted()){
            throw new RuntimeException("The requested product has been deleted");
        }
        return product;
    }

    /**
     * Retrieves all products.
     *
     * @return a list of {@link ProductDTO} objects representing the products.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntities = repository.findAllByIsDeletedFalse();
        return productEntities.stream()
                .map(productMapper::toDto)
                .toList();
    }


    /**
     * Retrieves the products of a specific store.
     *
     * @param storeId the ID of the store
     * @return a list of ReturnProductDTO objects representing the products of the store
     */
    @Transactional(readOnly = true)
    public List<ReturnProductDTO> getProductsByStoreId(Long storeId) {
        List<ProductEntity> productEntities = repository.findByStoreId(storeId);

        return productEntities.stream().map(productEntity -> {
            ResourceFileDTO fileDTO = null;
            if (productEntity.getResourceFile() != null) {
                try {
                    fileDTO = resourceFileService.downloadFile(productEntity.getResourceFile().getId());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return ProductHelper.convertToReturnProductDto(productEntity, fileDTO);
        }).toList();
    }



    /**
     * Adds a {@link ProductDTO} to the system and sends a notification to all users of the associated store.
     *
     * @param productDTO the {@link ProductDTO} object representing the product to be added
     * @param file the file to be processed and stored for the product (optional)
     * @return the {@link ProductDTO} object representing the added product
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public ReturnProductDTO addProduct(ProductDTO productDTO, MultipartFile file) throws IOException {
        ProductEntity productEntity = instantiateFileEntity(productDTO);

        if (file != null && !file.isEmpty()) {
            processUploadFileService.processUpload(file, productEntity);
        }

        productEntity.getStore().getUser().stream()
                .map(userMapper::toDto)
                .forEach(user -> {
                    NotificationDTO notificationDTO = new NotificationDTO();
                    notificationDTO.setSubject("New Product Addition");
                    notificationDTO.setIsSeen(false);
                    notificationDTO.setText("New product added");
                    notificationDTO.setIsDeleted(false);
                    String description = String.format("Product details: Name - %s, Model - %s, Category - %s",
                            productEntity.getName(), productEntity.getModel(), productEntity.getCategory().getName());
                    if (user.getTelegramId() != null) {
                        notificationDTO.setTelegramId(user.getTelegramId());
                    }
                    if (user.getIsTelegram() != null) {
                        notificationDTO.setIsTelegram(user.getIsTelegram());
                    }
                    notificationDTO.setDescription(description);

                    notificationDTO.setUser(userMapper.toDto(user));
                    notificationDTO.setNotificationLevel(Collections.singleton(NotificationLevel.SUCCESS));

                    notificationService.createNotification(notificationDTO);
                });

        return productMapper.toReturnDto(productEntity);
    }


    @Transactional
    public ReturnProductDTO updateProduct(Long productId, ProductDTO productDTO, MultipartFile file) throws IOException {
        ProductEntity existingEntity = repository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        productMapper.partialUpdate(productDTO, existingEntity);

        if (productDTO.getStore() != null) {
            StoreEntity storeEntity = storeRepository.findById(productDTO.getStore().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + productDTO.getStore().getId()));
            existingEntity.setStore(storeEntity);
        }

        if (productDTO.getCategory() != null) {
            CategoryEntity categoryEntity = categoryRepository.findById(productDTO.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productDTO.getCategory().getId()));
            existingEntity.setCategory(categoryEntity);
        }

        resourceFileService.handleFile(existingEntity, file, ResourceFileService.ProcessType.UPDATE);

        repository.save(existingEntity);

        ResourceFileDTO fileDTO = null;
        if (existingEntity.getResourceFile() != null) {
            try {
                fileDTO = resourceFileService.downloadFile(existingEntity.getResourceFile().getId());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return ProductHelper.convertToReturnProductDto(existingEntity, fileDTO);
    }


    /**
     * Deletes a product with the given product ID.
     *
     * @param productId the ID of the product to delete
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the product ID is null
     */
    @Transactional
    public void delete(Long productId) throws IOException {
        Objects.requireNonNull(productId, "Entity ID cannot be null");

        ProductEntity existingEntity = repository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + productId));

        resourceFileService.handleFile(existingEntity, null, ResourceFileService.ProcessType.DELETE);

        List<ProductFieldEntity> productFields = existingEntity.getProductFields();
        if(productFields != null){
            productFields.forEach(field -> field.setIsDeleted(true));
        }

        existingEntity.setIsDeleted(true);

        repository.save(existingEntity);
    }

    /**
     * Instantiate a {@link ProductEntity} object from a {@link ProductDTO} object.
     *
     * @param productDTO the {@link ProductDTO} object representing the product data transfer object to be instantiated
     * @return the instantiated {@link ProductEntity} object
     * @throws IllegalArgumentException if the store with the provided ID is not found
     */
    private ProductEntity instantiateFileEntity(ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.toEntity(productDTO);

        StoreEntity storeEntity = storeRepository.findById(productDTO.getStore().getId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + productEntity.getStore().getId()));


        productEntity.setStore(storeEntity);
        CategoryEntity categoryEntity = categoryRepository.findById(productDTO.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productDTO.getCategory().getId()));

        productEntity.setCategory(categoryEntity);

        repository.save(productEntity);
        return productEntity;
    }


}
