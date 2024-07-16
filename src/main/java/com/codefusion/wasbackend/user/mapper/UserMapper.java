package com.codefusion.wasbackend.user.mapper;

import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {


    /**
     * Converts a UserDTO object to a UserEntity object.
     *
     * @param userDTO the UserDTO object representing the user data transfer object to be converted
     * @return the UserEntity object representing the converted user entity
     */
    UserEntity toEntity(UserDTO userDTO);

    NotificationDTO.UserEntityDto toDto(UserDTO userDTO);

    /**
     * Converts a UserEntity object to a UserDTO object.
     *
     * @param userEntity the UserEntity object representing the user entity to be converted
     * @return the UserDTO object representing the converted user data transfer object
     */
    @Mapping(target = "resourceFile", source = "resourceFile")
    @Mapping(target = "roles", source = "account.roles")
    UserDTO toDto(UserEntity userEntity);

    /**
     * Updates the UserEntity object with the non-null properties from the UserDTO object.
     *
     * @param userDTO    the UserDTO object representing the updated user information
     * @param userEntity the UserEntity object to update
     * @return the updated UserEntity object
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(UserDTO userDTO, @MappingTarget UserEntity userEntity);

}
