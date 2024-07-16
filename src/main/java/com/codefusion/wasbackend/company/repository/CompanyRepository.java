package com.codefusion.wasbackend.company.repository;

import com.codefusion.wasbackend.company.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    @Query("SELECT c FROM CompanyEntity c WHERE c.isDeleted = false")
    List<CompanyEntity> findAllByIsDeletedFalse();

    @Query("SELECT c FROM CompanyEntity c WHERE c.id = :id AND c.isDeleted = false")
    Optional<CompanyEntity> findByIdAndIsDeletedFalse(@Param("id") Long id);
}
