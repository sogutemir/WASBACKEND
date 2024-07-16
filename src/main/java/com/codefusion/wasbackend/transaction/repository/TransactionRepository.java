package com.codefusion.wasbackend.transaction.repository;

import com.codefusion.wasbackend.transaction.dto.DailyTransactionTotalDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.id = :id AND t.isDeleted = false")
    Optional<TransactionEntity> findByIdAndIsDeletedFalse(@Param("id") Long id);

    
    /**
     * Retrieves all transactions with a specific product ID.
     *
     * @param productId the ID of the product
     * @return a list of TransactionEntity objects representing the transactions
     */
    @Query("SELECT t FROM TransactionEntity t WHERE t.product.id = :productId AND t.isDeleted = false")
    List<TransactionEntity> findByProductId (@Param("productId") Long productId);

    /**
     * Retrieves all TransactionEntity objects where the isDeleted property is set to false.
     *
     * @return a List of TransactionEntity objects.
     */
    @Query("SELECT t from TransactionEntity t where t.isDeleted = false")
    List<TransactionEntity> findAllByIsDeletedFalse();

    @Query("SELECT new com.codefusion.wasbackend.transaction.dto.DailyTransactionTotalDTO(t.date, SUM(CASE WHEN t.isBuying THEN -t.price * t.quantity ELSE t.price * t.quantity END)) " +
            "FROM TransactionEntity t " +
            "JOIN t.product p " +
            "WHERE p.store.id = :storeId AND t.date >= :startDate " +
            "GROUP BY t.date")
    List<DailyTransactionTotalDTO> findDailyTransactionTotalsByStoreIdAndDateAfter(@Param("storeId") Long storeId, @Param("startDate") LocalDate startDate);

}
