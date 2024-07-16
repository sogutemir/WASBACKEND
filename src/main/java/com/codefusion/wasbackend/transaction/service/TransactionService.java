package com.codefusion.wasbackend.transaction.service;

import com.codefusion.wasbackend.base.utils.ProcessUploadFileService;
import com.codefusion.wasbackend.notification.service.NotificationService;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.transaction.dto.DailyTransactionTotalDTO;
import com.codefusion.wasbackend.transaction.dto.ReturnTransactionDTO;
import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.helper.TransactionHelper;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.transaction.mapper.TransactionMapper;
import com.codefusion.wasbackend.transaction.repository.TransactionRepository;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper transactionMapper;
    private final ProcessUploadFileService processUploadFileService;
    private final TransactionHelper transactionHelper;
    private final ResourceFileService resourceFileService;

    public TransactionService(TransactionRepository repository, ProductRepository productRepository, UserMapper userMapper,
                               ResourceFileService resourceFileService, NotificationService notificationService,
                              TransactionMapper transactionMapper, ProcessUploadFileService processUploadFileService) {
        this.transactionMapper = transactionMapper;
        this.repository = repository;
        this.resourceFileService = resourceFileService;
        this.processUploadFileService = processUploadFileService;
        this.transactionHelper = new TransactionHelper(transactionMapper, productRepository, notificationService, repository, userMapper);
    }


    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return the TransactionDTO object representing the transaction
     * @throws RuntimeException if the transaction is not found
     */
    @Transactional(readOnly = true)
    public ReturnTransactionDTO getTransactionById(Long transactionId) {
        TransactionEntity transactionEntity = repository.findByIdAndIsDeletedFalse(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found or is deleted"));

        ResourceFileDTO fileDTO = null;
        if (transactionEntity.getResourceFile() != null) {
            try {
                fileDTO = resourceFileService.downloadFile(transactionEntity.getResourceFile().getId());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return TransactionHelper.convertToReturnTransactionDto(transactionEntity, fileDTO);
    }

    @Transactional(readOnly = true)
    public TransactionEntity getTransactionEntityById(Long transactionId){
        TransactionEntity transactionEntity = repository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (transactionEntity.getIsDeleted()) {
            throw new RuntimeException("The requested transaction has been deleted");
        }
        return transactionEntity;
    }


    /**
     * Retrieves all transactions.
     *
     * @return a list of TransactionDTO objects representing all transactions
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions(){
        List<TransactionEntity> transactionEntities = repository.findAllByIsDeletedFalse();
        return transactionEntities.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    /**
     * Retrieves all transactions associated with a specific store ID.
     *
     * @param productId the ID of the store
     * @return a list of TransactionDTO objects representing the transactions
     */
    @Transactional(readOnly = true)
    public List<ReturnTransactionDTO> getTransactionsByProductId(Long productId) {
        List<TransactionEntity> transactionEntities = repository.findByProductId(productId);
        return transactionEntities.stream().map(transactionEntity -> {
            ResourceFileDTO fileDTO = null;
            if (transactionEntity.getResourceFile() != null) {
                try {
                    fileDTO = resourceFileService.downloadFile(transactionEntity.getResourceFile().getId());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return TransactionHelper.convertToReturnTransactionDto(transactionEntity, fileDTO);
        }).toList();
    }


    @Transactional(readOnly = true)
    public List<DailyTransactionTotalDTO> getDailyTransactionTotalsByStoreId(Long storeId) {
        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);
        return repository.findDailyTransactionTotalsByStoreIdAndDateAfter(storeId, tenDaysAgo);
    }

    /**
     * Adds a new transaction.
     *
     * @param transactionDTO the data transfer object representing the transaction
     * @param file the file associated with the transaction
     * @return the data transfer object representing the added transaction
     * @throws IOException if there is an error with the file operation
     */
    @Transactional
    public TransactionDTO addTransaction(TransactionDTO transactionDTO, MultipartFile file) throws IOException {
        TransactionEntity transactionEntity = transactionHelper.instantiateFileEntity(transactionDTO);
        if (file != null && !file.isEmpty()) {
            processUploadFileService.processUpload(file, transactionEntity);
        }

        return transactionMapper.toDto(transactionEntity);
    }

//    /**
//     * Updates an existing transaction.
//     *
//     * @param transactionId the ID of the transaction to update
//     * @param transactionDTO the data transfer object representing the updated transaction
//     * @param file the file associated with the transaction
//     * @return the data transfer object representing the updated transaction
//     * @throws IOException if there is an error with the file operation
//     */
//    @Transactional
//    public TransactionDTO updateTransaction(Long transactionId, TransactionDTO transactionDTO, MultipartFile file) throws IOException {
//        Objects.requireNonNull(transactionId, "Transaction ID cannot be null");
//        Objects.requireNonNull(transactionDTO, "Transaction DTO cannot be null");
//
//        TransactionEntity existingEntity = repository.findById(transactionId)
//                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionId));
//
//        transactionMapper.partialUpdate(transactionDTO, existingEntity);
//
//        handleFile(existingEntity, file, ProcessType.UPDATE);
//
//        // Update stock based on isBuying
//        if (existingEntity.getIsBuying() != null && existingEntity.getIsBuying()) {
//            existingEntity.getProduct().setCurrentStock(existingEntity.getProduct().getCurrentStock() + existingEntity.getQuantity());
//        }
//
//        repository.flush();
//        TransactionEntity updatedEntity = repository.save(existingEntity);
//
//        return transactionMapper.toDto(updatedEntity);
//    }


    /**
     * Deletes a transaction entity with the given transaction ID.
     *
     * @param transactionId the ID of the transaction entity to delete
     * @throws IOException              if there is an error with the file operation
     * @throws NullPointerException    if the transaction ID is null
     */
    @Transactional
    public void delete(Long transactionId) throws IOException {
        Objects.requireNonNull(transactionId, "Transaction ID cannot be null");

        TransactionEntity existingEntity = repository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + transactionId));

        // Handle profit and stock adjustment
        if (existingEntity.getIsBuying() != null) {
            if (!existingEntity.getIsBuying()) {
                existingEntity.getProduct().setCurrentStock(existingEntity.getProduct().getCurrentStock() - existingEntity.getQuantity());
                existingEntity.getProduct().setProfit(existingEntity.getProduct().getProfit() - (existingEntity.getPrice() * existingEntity.getQuantity()));
            } else {
                existingEntity.getProduct().setProfit(existingEntity.getProduct().getProfit() + (existingEntity.getPrice() * existingEntity.getQuantity()));
            }
        }

        resourceFileService.handleFile(existingEntity, null, ResourceFileService.ProcessType.DELETE);

        existingEntity.setIsDeleted(true);

        repository.save(existingEntity);
    }



}
