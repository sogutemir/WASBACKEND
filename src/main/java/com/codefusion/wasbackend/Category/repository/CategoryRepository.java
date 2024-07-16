package com.codefusion.wasbackend.Category.repository;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c WHERE c.store.id = :storeId")
    List<CategoryEntity> findAllByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT c FROM CategoryEntity c")
    List<CategoryEntity> findAllCategories();
}
