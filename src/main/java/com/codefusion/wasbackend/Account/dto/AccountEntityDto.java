package com.codefusion.wasbackend.Account.dto;

import com.codefusion.wasbackend.Account.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * DTO for {@link com.codefusion.wasbackend.Account.model.AccountEntity}
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString
@Builder
public class AccountEntityDto implements Serializable {

    private Long id;
    private UserEntityDto user;
    private Set<Role> roles;
    private String username;
    private String password;

    /**
     * DTO for {@link com.codefusion.wasbackend.user.model.UserEntity}
     */
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    @Setter
    @Getter
    @Builder
    @ToString
    public static class UserEntityDto implements Serializable {
        private Long id;
        private Boolean isDeleted;
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
        private String ActivationRequestCode;
        private Date telegramLinkTime;
    }
}