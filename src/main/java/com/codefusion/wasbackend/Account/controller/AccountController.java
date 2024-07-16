package com.codefusion.wasbackend.Account.controller;

import com.codefusion.wasbackend.Account.dto.AccountEntityDto;
import com.codefusion.wasbackend.Account.service.AccountService;
import com.codefusion.wasbackend.Account.service.AuthenticationService;
import com.codefusion.wasbackend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    /**
     * Log in with the given account entity.
     *
     * @param entity the AccountEntityDto object representing the account entity to log in with
     * @return the ResponseEntity<?> representing the login result
     * @throws Exception if there is an error during the login process
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountEntityDto entity) throws Exception {
        String token = authenticationService.login(entity);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Adds a new user.
     *
     * @param userDTO            the user data transfer object containing user information
     * @param file               the profile picture file of the user
     * @param accountEntityDto   the account entity data transfer object
     * @return the ResponseEntity<AccountEntityDto> representing the saved account entity
     * @throws IOException       if there is an error with the file operation
     */
    @PostMapping(value = "/addAccount", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AccountEntityDto> addUser(@ModelAttribute UserDTO userDTO,
                                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                                    @ModelAttribute AccountEntityDto accountEntityDto) throws IOException {
        return new ResponseEntity<>(accountService.createAccount(userDTO, file, accountEntityDto), HttpStatus.CREATED);
    }

}
