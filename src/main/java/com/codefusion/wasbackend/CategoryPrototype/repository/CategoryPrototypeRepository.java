package com.codefusion.wasbackend.CategoryPrototype.repository;

import com.codefusion.wasbackend.CategoryPrototype.model.CategoryPrototypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryPrototypeRepository extends JpaRepository<CategoryPrototypeEntity, Long> {

    @Query("SELECT cp from CategoryPrototypeEntity cp where cp.category.id = ?1 and cp.isDelete = false")
    List<CategoryPrototypeEntity> findByCategoryIdAndIsDeleteFalse(Long categoryId);

}