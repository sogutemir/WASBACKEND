package com.codefusion.wasbackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.codefusion.wasbackend.company.model.CompanyEntity}
 */
@AllArgsConstructor
@Getter
@ToString
@Builder
@Setter
@NoArgsConstructor
public class CreateCompanyDto implements Serializable {
    private Long id;
    private Boolean isDeleted;
    private ResourceFileEntityDto resourceFile;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    private String description;
    private String taxLevel;
    private Long userId;


    /**
     * DTO for {@link com.codefusion.wasbackend.resourceFile.model.ResourceFileEntity}
     */
    @AllArgsConstructor
    @Getter
    @ToString
    @Builder
    @Setter
    @NoArgsConstructor
    public static class ResourceFileEntityDto implements Serializable {
        private Long id;
        private String name;
        private String type;
        private Boolean isDeleted;
        private byte[] data;
        private LocalDateTime uploadDate;
    }
}