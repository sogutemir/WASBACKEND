package com.codefusion.wasbackend.transaction.helper;

import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.model.NotificationLevel;
import com.codefusion.wasbackend.notification.service.NotificationService;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.repository.ProductRepository;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.transaction.dto.ReturnTransactionDTO;
import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.mapper.TransactionMapper;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.transaction.repository.TransactionRepository;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.mapper.UserMapper;
import com.codefusion.wasbackend.user.model.UserEntity;

import java.util.Collections;
import java.util.List;

public class TransactionHelper {

    private final TransactionMapper transactionMapper;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;
    private final TransactionRepository transactionRepository;
    private final UserMapper userMapper;

    public TransactionHelper(TransactionMapper transactionMapper, ProductRepository productRepository, NotificationService notificationService,
                             TransactionRepository transactionRepository, UserMapper userMapper) {
        this.transactionMapper = transactionMapper;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.transactionRepository = transactionRepository;
        this.userMapper = userMapper;
    }


    public static ReturnTransactionDTO convertToReturnTransactionDto(TransactionEntity transactionEntity, ResourceFileDTO fileDTO) {
        ReturnTransactionDTO.ResourceFileDto resourceFileDto = null;
        if (fileDTO != null) {
            resourceFileDto = mapResourceFile(fileDTO);
        }

        return ReturnTransactionDTO.builder()
                .id(transactionEntity.getId())
                .isDeleted(transactionEntity.getIsDeleted())
                .isBuying(transactionEntity.getIsBuying())
                .date(transactionEntity.getDate())
                .price(transactionEntity.getPrice())
                .fullName(transactionEntity.getFullName())
                .quantity(transactionEntity.getQuantity())
                .address(transactionEntity.getAddress())
                .phone(transactionEntity.getPhone())
                .product(transactionEntity.getProduct() != null ? mapProduct(transactionEntity.getProduct()) : null)
                .resourceFile(resourceFileDto)
                .build();
    }

    public static ReturnTransactionDTO.ResourceFileDto mapResourceFile(ResourceFileDTO fileDTO) {
        return ReturnTransactionDTO.ResourceFileDto.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getFileName())
                .type(fileDTO.getContentType())
                .data(fileDTO.getData())
                .build();
    }

    private static ReturnTransactionDTO.ProductDTO mapProduct(ProductEntity productEntity) {
        return ReturnTransactionDTO.ProductDTO.builder()
                .id(productEntity.getId())
                .isDeleted(productEntity.getIsDeleted())
                .name(productEntity.getName())
                .model(productEntity.getModel())
                .currentStock(productEntity.getCurrentStock())
                .profit(productEntity.getProfit())
                .productCode(productEntity.getProductCode())
                .build();
    }

    /**
     * Creates a new TransactionEntity object based on the given TransactionDTO object.
     *
     * @param transactionDTO the TransactionDTO object to create a TransactionEntity from
     * @return the created TransactionEntity object
     */
    public TransactionEntity instantiateFileEntity(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = transactionMapper.toEntity(transactionDTO);

        ProductEntity productEntity = productRepository.findById(transactionDTO.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + transactionDTO.getProduct().getId()));

        transactionEntity.setProduct(productEntity);

        List<UserEntity> userEntityList = transactionEntity.getProduct().getStore().getUser();

        for (UserEntity userEntity : userEntityList) {
            UserDTO user = userMapper.toDto(userEntity);
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setSubject("New Transaction");
            notificationDTO.setText("New transaction occurred");
            String description = String.format("Transaction details: Product - %s, Store - %s, Profit - %s, Quantity - %s",
                    transactionEntity.getProduct().getName(), transactionEntity.getProduct().getStore().getName(),
                    transactionEntity.getProduct().getProfit(), transactionEntity.getQuantity());
            if (user.getTelegramId() != null) {
                notificationDTO.setTelegramId(user.getTelegramId());
            }
            if (user.getIsTelegram() != null) {
                notificationDTO.setIsTelegram(user.getIsTelegram());
            }
            notificationDTO.setDescription(description);
            notificationDTO.setIsDeleted(false);
            notificationDTO.setIsSeen(false);
            notificationDTO.setUser(userMapper.toDto(user));
            if (transactionEntity.getProduct().getProfit() > 0) {
                notificationDTO.setNotificationLevel(Collections.singleton(NotificationLevel.SUCCESS));
            } else {
                notificationDTO.setNotificationLevel(Collections.singleton(NotificationLevel.WARNING));
            }

            notificationService.createNotification(notificationDTO);
        }

        transactionRepository.save(transactionEntity);

        return transactionEntity;
    }
}
