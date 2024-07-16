package com.codefusion.wasbackend.user.repository;

import com.codefusion.wasbackend.Account.model.Role;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Retrieves a list of user entities by the ID of the store they are associated with.
     *
     * @param storeId the ID of the store
     * @return a List of UserEntity objects matching the given store ID
     */
    @Query("SELECT u FROM UserEntity u JOIN u.stores s WHERE s.id = :storeId AND u.isDeleted = false")
    List<UserEntity> findByStoreId (@Param("storeId") Long storeId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM UserEntity u WHERE u.account.username = :username")
    boolean existsByUsername(@Param("username") String username);

    /**
     * Retrieves a user entity by ID.
     *
     * @param id the ID of the user
     * @return an Optional<UserEntity> object representing the user with the given ID, if found
     */
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.isDeleted = false")
    Optional<UserEntity> findById (@Param("id") Long id);

    @Query("SELECT u FROM UserEntity u JOIN u.account a JOIN a.roles r WHERE r = 'EMPLOYEE'")
    List<UserEntity> findAllEmployees();

    /**
     * Retrieves a user entity based on the ID of the store they are associated with and the specified roles.
     *
     * @param storeId the ID of the store
     * @param roles   the list of roles to filter by
     * @return a UserEntity object matching the given store ID and roles
     */
    @Query("SELECT u FROM UserEntity u JOIN u.stores s JOIN u.account a JOIN a.roles r WHERE s.id = :storeId AND r IN (:roles) AND u.isDeleted = false")
    UserEntity findByStoreIdAndRoles(@Param("storeId") Long storeId, @Param("roles") List<Role> roles);


}
