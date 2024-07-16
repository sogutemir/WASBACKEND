package com.codefusion.wasbackend.resourceFile.repository;

import com.codefusion.wasbackend.resourceFile.model.ResourceFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResourceFileRepository extends JpaRepository<ResourceFileEntity, Long> {

    /**
     * Finds a resource file by its name
     *
     * @param name the name of the resource file
     * @return an Optional<ResourceFileEntity> containing the resource file if found, otherwise empty
     */
    @Query("SELECT f FROM ResourceFileEntity f WHERE f.name = :name")
    Optional<ResourceFileEntity> findByName(@Param("name") String name);

    /**
     * Retrieves a resource file entity by its ID.
     *
     * @param id the ID of the resource file entity
     * @return an Optional containing the resource file entity if found, otherwise empty
     */
    @Query("SELECT f FROM ResourceFileEntity f WHERE f.id = :id")
    Optional<ResourceFileEntity> findById(@Param("id") Long id);




}
