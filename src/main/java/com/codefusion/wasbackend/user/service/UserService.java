package com.codefusion.wasbackend.user.service;

import com.codefusion.wasbackend.Account.model.AccountEntity;
import com.codefusion.wasbackend.Account.model.Role;
import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.product.mapper.ProductMapper;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import com.codefusion.wasbackend.store.mapper.StoreMapper;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.repository.StoreRepository;
import com.codefusion.wasbackend.user.dto.EmployeeProfitDTO;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.helper.UserHelper;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a UserService that provides operations related to User entities.
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final StoreMapper storeMapper;
    private final ProductMapper productMapper;
    private final UserRepository repository;
    private final ResourceFileService resourceFileService;
    private final StoreRepository storeRepository;

    public UserService(UserRepository userRepository, ResourceFileService resourceFileService, StoreRepository storeRepository,
                       UserMapper userMapper, StoreMapper storeMapper, ProductMapper productMapper) {
        this.userMapper = userMapper;
        this.resourceFileService = resourceFileService;
        this.storeRepository = storeRepository;
        this.repository = userRepository;
        this.storeMapper = storeMapper;
        this.productMapper = productMapper;
    }


    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return the UserDTO object representing the user with the given ID
     * @throws RuntimeException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use the helper method to convert UserEntity to UserDTO
        UserDTO.ResourceFileEntityDto resourceFileDto = null;
        if (userEntity.getResourceFile() != null) {
            try {
                ResourceFileDTO fileDTO = resourceFileService.downloadFile(userEntity.getResourceFile().getId());
                resourceFileDto = UserHelper.mapResourceFile(fileDTO);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return UserHelper.convertToUserDTO(userEntity, resourceFileDto);
    }

    /**
     * Retrieves a {@link UserEntity} by ID.
     *
     * @param id the ID of the user
     * @return the {@link UserEntity} object representing the user with the given ID
     * @throws RuntimeException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserEntity getUserEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Retrieves a user by ID for account creation.
     *
     * @param id the ID of the user
     * @return the UserEntity object representing the user with the given ID
     * @throws RuntimeException if the user is not found
     */
    @Transactional(readOnly = true)
    public UserEntity getUserByIdforAccount(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Retrieves managers and employees based on the given store ID.
     *
     * @param storeId the ID of the store
     * @return a*/
    @Transactional(readOnly = true)
    public UserDTO getManagersAndEmployees(Long storeId){
        return userMapper.toDto(repository.findByStoreIdAndRoles(storeId, Arrays.asList(Role.EMPLOYEE, Role.MANAGER)));
    }

    /**
     * Retrieves all users.
     *
     * @return a List of UserDTO objects representing all users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(){
        List<UserEntity> userEntities = repository.findAll();
        return userEntities.stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Retrieves all products associated with a user.
     *
     * @param userId the ID of the user
     * @return a List of ProductDTO objects representing all products associated with the user
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllUserProducts(Long userId) {
        Optional<UserEntity> userEntityOpt = repository.findById(userId);
        List<ProductDTO> allProductDTOs = new ArrayList<>();

        if (userEntityOpt.isPresent()) {
            List<StoreDTO> userStoreDTOs = userEntityOpt.get().getStores().stream()
                    .map(storeMapper::toDto)
                    .toList();

            for (StoreDTO storeDTO : userStoreDTOs) {
                List<ProductDTO> productDTOs = storeDTO.getProducts().stream()
                        .map(productMapper::toDto)
                        .toList();

                allProductDTOs.addAll(productDTOs);
            }
        }

        return allProductDTOs;
    }

    /**
     * Retrieves users by store ID.
     *
     * @param storeId the ID of the store
     * @return a List of UserDTO objects representing the users with the given store ID
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByStoreId(Long storeId) {
        List<UserEntity> userEntities = repository.findByStoreId(storeId);

        Optional<UserEntity> bossOptional = userEntities.stream()
                .filter(user -> user.getAccount().getRoles().contains(Role.BOSS))
                .findFirst();

        if (bossOptional.isPresent()) {
            Long bossId = bossOptional.get().getId();
            userEntities = userEntities.stream()
                    .filter(user -> Objects.equals(user.getOwnerId(), bossId))
                    .toList();
        }

        return userEntities.stream().map(userEntity -> {
            UserDTO.ResourceFileEntityDto resourceFileDto = null;
            if (userEntity.getResourceFile() != null) {
                try {
                    ResourceFileDTO fileDTO = resourceFileService.downloadFile(userEntity.getResourceFile().getId());
                    resourceFileDto = UserHelper.mapResourceFile(fileDTO);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return UserHelper.convertToUserDTO(userEntity, resourceFileDto);
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<EmployeeProfitDTO> getTop3EmployeesByStoreProfit() {
        List<UserEntity> userEntities = repository.findAllEmployees();

        return userEntities.stream()
                .map(user -> {
                    double totalProfit = user.getStores().stream()
                            .flatMap(store -> store.getProducts().stream())
                            .mapToDouble(product -> product.getProfit())
                            .sum();
                    return EmployeeProfitDTO.builder()
                            .name(user.getName())
                            .surname(user.getSurname())
                            .totalProfit(totalProfit)
                            .build();
                })
                .sorted(Comparator.comparingDouble(EmployeeProfitDTO::getTotalProfit).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }



    /**
     * Adds a new user.
     *
     * @param userDTO the user data transfer object containing user information
     * @param file the profile picture file of the user
     * @return the UserDTO object representing the user that was added
     * @throws IOException if there is an error with the file operation
     */

    @Transactional
    public UserDTO addUser(UserDTO userDTO, MultipartFile file) throws IOException {
        Objects.requireNonNull(userDTO, "DTO cannot be null");

        UserEntity newEntity = userMapper.toEntity(userDTO);
        if (newEntity != null) {
            List<Long> storeIds = userDTO.getStoreIds();
            if (storeIds != null && !storeIds.isEmpty()) {
                List<StoreEntity> stores = storeRepository.findAllById(storeIds);
                newEntity.setStores(stores);
            }
            newEntity.setIsDeleted(false);
            newEntity = repository.save(newEntity);
            resourceFileService.handleFile(newEntity, file, ResourceFileService.ProcessType.ADD);
        }
        return userMapper.toDto(newEntity);
    }

    @Transactional
    public UserDTO update(Long entityId, UserDTO dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(entityId, "Entity ID cannot be null");
        Objects.requireNonNull(dto, "DTO cannot be null");

        UserEntity existingEntity = repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));

        resourceFileService.handleFile(existingEntity, file, ResourceFileService.ProcessType.UPDATE);

        userMapper.partialUpdate(dto, existingEntity);

        if (dto.getStoreIds() != null && !dto.getStoreIds().isEmpty()) {
            List<StoreEntity> stores = storeRepository.findAllById(dto.getStoreIds());
            existingEntity.setStores(stores);
        }

        updateRoles(existingEntity, dto.getRoles());

        UserEntity updatedEntity = repository.save(existingEntity);

        return userMapper.toDto(updatedEntity);
    }

    /**
     * Deletes a user with the given user ID.
     *
     * @param userId the ID of the user to delete
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the user ID is null
     */
    @Transactional
    public void delete(Long userId) throws IOException {

        Objects.requireNonNull(userId, "Entity ID cannot be null");

        UserEntity existingEntity = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + userId));

        resourceFileService.handleFile(existingEntity, null, ResourceFileService.ProcessType.DELETE);

        existingEntity.setIsDeleted(true);

        repository.save(existingEntity);

    }

    private void updateRoles(UserEntity entity, Set<Role> roles) {
        if (entity.getAccount() != null) {
            entity.getAccount().setRoles(roles);
        } else {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setUser(entity);
            accountEntity.setRoles(roles);
            entity.setAccount(accountEntity);
        }
    }

}
