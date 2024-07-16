package com.codefusion.wasbackend.product.repository;

import com.codefusion.wasbackend.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id AND p.isDeleted = false")
    Optional<ProductEntity> findByIdAndIsDeletedFalse(@Param("id") Long id);


    /**
     * Retrieves a list of ProductEntity objects by the ID of the store they belong to.
     *
     * @param storeId the ID of the store
     * @return a list of ProductEntity objects corresponding to the products of the store
     */
    @Query("SELECT p FROM ProductEntity p WHERE p.store.id = :storeId AND p.isDeleted = false")
    List<ProductEntity> findByStoreId (@Param("storeId") Long storeId);


    /**
     * Retrieves all products that are not deleted.
     *
     * @return a list of {@link ProductEntity} objects representing the products
     */
    @Query("SELECT p FROM ProductEntity p WHERE p.isDeleted = false")
    List<ProductEntity> findAllByIsDeletedFalse();

}
