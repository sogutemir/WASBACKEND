package com.codefusion.wasbackend.resourceFile.service;


import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.model.ResourceFileEntity;
import com.codefusion.wasbackend.resourceFile.repository.ResourceFileRepository;
import com.codefusion.wasbackend.resourceFile.utility.DetermineResourceFileType;
import com.codefusion.wasbackend.resourceFile.utility.ResourceFileUtils;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class ResourceFileService {

    private final ResourceFileRepository fileRepository;

    public enum ProcessType {
        ADD, DELETE, UPDATE
    }


    private static final String ENTITY_CANNOT_BE_NULL = "Entity cannot be null";
    private static final String FILE_EMPTY = "File is empty";
    private static final String FILE_NOT_FOUND_MSG = "File not found with id: ";
    private static final String DELETION_ERROR_MSG = "Error occurred while deleting the file with id: ";
    public static final String UPDATED_MSG = "Updated file in DB with name: %s";

    /**
     * Saves the file in the database with the associated entity.
     *
     * @param file   the file to be saved
     * @param entity the entity to associate the file with
     * @return a string indicating the successful file save
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public String saveFile(MultipartFile file, BaseEntity entity) throws IOException {
        validateFileAndEntity(file, entity);

        ResourceFileEntity fileEntity = createResourceFileEntity(file);
        fileEntity.setIsDeleted(false);
        associateEntityWithFile(entity, fileEntity);
        fileRepository.save(fileEntity);
        return "Saved file in DB with name: " + fileEntity.getName();
    }

    /**
     * Validates the file and entity before processing.
     *
     * @param file   The multipart file to be validated.
     * @param entity The base entity to be validated.
     * @throws IllegalArgumentException If the entity is null or the file is empty.
     */
    private void validateFileAndEntity(MultipartFile file, BaseEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(ENTITY_CANNOT_BE_NULL);
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException(FILE_EMPTY);
        }
    }

    /**
     * Validates the given file.
     *
     * @param file the file to validate
     * @throws IllegalArgumentException if the file is empty
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException(FILE_EMPTY);
        }
    }

    /**
     * Creates a ResourceFileEntity from the given MultipartFile.
     * The file name is cleaned using StringUtils.cleanPath.
     * The file type is obtained from the content type of the MultipartFile.
     * The file data is compressed using ResourceFileUtils.compressBytes.
     *
     * @param file the MultipartFile to create the ResourceFileEntity from
     * @return the created ResourceFileEntity
     * @throws IOException if an I/O error occurs
     */
    private ResourceFileEntity createResourceFileEntity(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String contentType = file.getContentType();
        byte[] compressedData = ResourceFileUtils.compressBytes(file.getBytes());

        return ResourceFileEntity.builder()
                .name(fileName)
                .type(contentType)
                .data(compressedData)
                .build();
    }

    /**
     * Retrieves the name of a file given its ID.
     *
     * @param fileId the ID of the file
     * @return the name of the file
     * @throws FileNotFoundException if the file with the provided ID does not exist
     */
    @Transactional(readOnly = true)
    public String getFileName(Long fileId) throws FileNotFoundException {
        return fileRepository.findById(fileId)
                .map(ResourceFileEntity::getName)
                .orElseThrow(() -> new FileNotFoundException(FILE_NOT_FOUND_MSG + fileId));
    }

    /**
     * Updates the file associated with a resource file entity.
     *
     * @param id   the ID of the resource file entity to update
     * @param file the updated file to associate with the resource file entity
     * @return a message indicating that the file has been updated
     * @throws IOException               if there is an error with the file operation
     * @throws FileNotFoundException     if the resource file entity with the given ID is not found
     * @throws IllegalArgumentException if the file is empty
     */
    @Transactional
    public String updateFile(Long id, MultipartFile file) throws IOException {
        validateFile(file);
        ResourceFileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(String.format(FILE_NOT_FOUND_MSG, id)));
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileEntity.setName(fileName);
        fileEntity.setType(file.getContentType());
        fileEntity.setData(ResourceFileUtils.compressBytes(file.getBytes()));
        fileEntity.setIsDeleted(false);
        fileRepository.save(fileEntity);
        return String.format(UPDATED_MSG, fileName);
    }

    /**
     * Downloads a file from the file repository based on the specified file ID.
     *
     * @param fileId the ID of the file to be downloaded
     * @return a ResourceFileDTO object containing the downloaded file data and filename
     * @throws FileNotFoundException if the file with the specified ID is not found in the repository
     */
    @Transactional(readOnly = true)
    public ResourceFileDTO downloadFile(Long fileId) throws FileNotFoundException {
        ResourceFileEntity retrievedFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(FILE_NOT_FOUND_MSG + fileId));
        byte[] decompressedData = ResourceFileUtils.decompressBytes(retrievedFile.getData());
        return new ResourceFileDTO(decompressedData, retrievedFile.getName());
    }

    /**
     * Associates the given entity with the given file entity.
     *
     * @param entity the entity to associate with the file entity
     * @param fileEntity the file entity to associate with the entity
     * @throws IllegalArgumentException if the entity type is not supported
     */
    public void associateEntityWithFile(BaseEntity entity, ResourceFileEntity fileEntity) {
        String entityClassName = entity.getClass().getSimpleName();

        switch (entityClassName) {
            case "ProductEntity":
                ((ProductEntity) entity).setResourceFile(fileEntity);
                break;
            case "UserEntity":
                ((UserEntity) entity).setResourceFile(fileEntity);
                break;
            case "StoreEntity":
                ((StoreEntity) entity).setResourceFile(fileEntity);
                break;
            case "TransactionEntity":
                ((TransactionEntity) entity).setResourceFile(fileEntity);
                break;
            case "CompanyEntity":
                ((CompanyEntity) entity).setResourceFile(fileEntity);
                break;
            default:
                throw new IllegalArgumentException("Entity type not supported");
        }
    }

    /**
     * Retrieves the ID of the resource file associated with the given entity.
     *
     * @param entity the BaseEntity instance representing the entity
     * @return the ID of the resource file associated with the entity
     * @throws FileNotFoundException if the entity type is not supported or the resource file is not found
     */
    @Transactional(readOnly = true)
    public Long findResourceFileId(BaseEntity entity) throws FileNotFoundException {
        String entityClassName = entity.getClass().getSimpleName();
        Long Id = switch (entityClassName) {
            case "ProductEntity" -> ((ProductEntity) entity).getResourceFile().getId();
            case "UserEntity" -> ((UserEntity) entity).getResourceFile().getId();
            case "StoreEntity" -> ((StoreEntity) entity).getResourceFile().getId();
            case "TransactionEntity" -> ((TransactionEntity) entity).getResourceFile().getId();
            case "CompanyEntity" -> ((CompanyEntity) entity).getResourceFile().getId();
            default -> throw new IllegalArgumentException("Entity type not supported");
        };
        return Id;
    }


    /**
     * Deletes a file with the given fileId.
     *
     * @param fileId the ID of the file to delete
     * @throws FileNotFoundException if the file with the given ID is not found
     */
    @Transactional
    public void deleteFile(Long fileId) throws FileNotFoundException {
        ResourceFileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(FILE_NOT_FOUND_MSG + fileId));
        try {
            file.setIsDeleted(true);
            fileRepository.save(file);
        } catch (Exception e) {
            throw new ServiceException(DELETION_ERROR_MSG + fileId, e);
        }
    }

    /**
     * Retrieves a resource file based on the specified file ID.
     *
     * @param fileId the ID of the file to be retrieved
     * @return the response builder for the resource file
     * @throws FileNotFoundException if the file with the specified ID is not found
     */
    public ResponseEntity.BodyBuilder retrieveResourceFile(Long fileId) throws FileNotFoundException {
        ResourceFileDTO fileDto = downloadFile(fileId);
        String fileType = fileDto.getFileName().substring(fileDto.getFileName().lastIndexOf('.') + 1);
        String contentType = DetermineResourceFileType.determineFileType(fileType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType));
    }

    public void handleFile(BaseEntity existingEntity, MultipartFile file, ProcessType processType) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (processType == ProcessType.UPDATE && existingEntity.getResourceFile() != null) {
                Long oldFileId = existingEntity.getResourceFile().getId();
                updateFile(oldFileId, file);
            } else if (processType == ProcessType.ADD) {
                saveFile(file, existingEntity);
            } else {
                throw new IllegalArgumentException("Invalid process type");
            }
        } else if (processType == ProcessType.DELETE && existingEntity.getResourceFile() != null) {
            Long oldFileId = existingEntity.getResourceFile().getId();
            deleteFile(oldFileId);
        }
    }

}