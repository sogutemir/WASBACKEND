package com.codefusion.wasbackend.productField.repository;

import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductFieldRepository extends JpaRepository<ProductFieldEntity, Long> {

    /**
     * Retrieves a list of {@link ProductFieldEntity} objects associated with a given productId.
     *
     * @param productId the ID of the product
     * @return a list of ProductFieldEntity objects associated with the given productId
     */
    @Query("SELECT p FROM ProductFieldEntity p WHERE p.product.id = :productId AND p.isDeleted = false")
    List<ProductFieldEntity> findByProductId (@Param("productId") Long productId);

    /**
     * Retrieves all non-deleted {@link ProductFieldEntity} objects.
     *
     * @return a list of ProductFieldEntity objects that are not deleted
     */
    @Query("SELECT p FROM ProductFieldEntity p WHERE p.isDeleted = false")
    List<ProductFieldEntity> findAllByIsDeletedFalse();

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductFieldEntity p WHERE p.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
