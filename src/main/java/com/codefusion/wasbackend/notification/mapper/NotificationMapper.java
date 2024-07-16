package com.codefusion.wasbackend.notification.mapper;


import com.codefusion.wasbackend.notification.dto.NotificationDTO;
import com.codefusion.wasbackend.notification.model.NotificationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    NotificationDTO toDto(NotificationEntity notificationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    NotificationEntity partialUpdate(NotificationDTO notificationDTO, @MappingTarget NotificationEntity notificationEntity);

    NotificationEntity toEntity(NotificationDTO notificationDTO);
}