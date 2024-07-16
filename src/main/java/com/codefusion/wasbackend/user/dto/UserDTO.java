package com.codefusion.wasbackend.user.dto;

import com.codefusion.wasbackend.Account.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDTO implements Serializable {
    private Long id;
    private Boolean isDeleted;
    private ResourceFileEntityDto resourceFile;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "Surname cannot be empty")
    private String surname;
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Phone cannot be empty")
    private String phoneNo;
    private Long telegramId;
    private Boolean isTelegram;
    private String activationRequestCode;
    private Date telegramLinkTime;
    private List<StoreEntityDto> stores;
    private List<Long> storeIds;
    private Set<Role> roles;
    private AccountEntityDto account;
    private Long ownerId;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class ResourceFileEntityDto implements Serializable {
        private Long id;
        private String name;
        private String type;
        private Boolean isDeleted;
        private byte[] data;
        private LocalDateTime uploadDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class StoreEntityDto implements Serializable {
        private Long id;
        private Boolean isDeleted;
        @NotBlank(message = "Name cannot be empty")
        private String name;
        private String description;
        @NotBlank(message = "Address cannot be empty")
        private String address;
        private String storePhoneNo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountEntityDto implements Serializable {
        private Long id;
        private Set<Role> roles;
        private String username;
        private String password;
    }
}
