package com.codefusion.wasbackend.user.controller;

import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.user.dto.EmployeeProfitDTO;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ResourceFileService resourceFileService;

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return the UserDTO object representing the user with the given ID
     * @throws RuntimeException if the user is not found
     */
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Retrieves all users.
     *
     * @return a List of UserDTO objects representing all users
     */
    @GetMapping("/allUser")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/top3EmployeesByStoreProfit")
    public ResponseEntity<List<EmployeeProfitDTO>> getTop3EmployeesByStoreProfit() {
        return ResponseEntity.ok(userService.getTop3EmployeesByStoreProfit());
    }

    /**
     * Retrieves all products associated with a user.
     *
     * @param userId the ID of the user
     * @return a List of ProductDTO objects representing all products associated with the user
     */
    @GetMapping("/{userId}/products")
    public List<ProductDTO> getAllUserProducts(@PathVariable Long userId) {
        return userService.getAllUserProducts(userId);
    }

    /**
     * Downloads the resource file associated with the given user ID.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity object containing the resource file data and appropriate headers for file download
     * @throws FileNotFoundException if the resource file is not found
     */
    @GetMapping("/downloadResourceFile/{userId}")
    public ResponseEntity<byte[]> downloadUserResourceFile(@PathVariable Long userId) throws FileNotFoundException {
        UserEntity userEntity = userService.getUserEntityById(userId);
        Long fileId = resourceFileService.findResourceFileId(userEntity);
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
        ResponseEntity.BodyBuilder responseBuilder = resourceFileService.retrieveResourceFile(fileId);
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getData());
    }

    /**
     * Retrieves users by store ID.
     *
     * @param storeId the ID of the store
     * @return a List of UserDTO objects representing the users with the given store ID
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserDTO>> getUsersByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(userService.getUsersByStoreId(storeId));
    }

    /**
     * Retrieves users by store ID and roles.
     *
     * @param storeId the ID of the store
     * @return a ResponseEntity containing a List of UserDTO objects representing the users with the given store ID and roles
     */
    @GetMapping("/storeWithRole/{storeId}")
    public ResponseEntity<List<UserDTO>> getUsersByStoreIdAndRoles(@PathVariable Long storeId){
        return ResponseEntity.ok(Collections.singletonList(userService.getManagersAndEmployees(storeId)));
    }

    /**
     * Adds a new user.
     *
     * @param userDTO the user data transfer object containing user information
     * @param file the profile picture file of the user
     * @return the UserDTO object representing the user that was added
     * @throws IOException if there is an error with the file operation
     */
    @PostMapping(value = "/addUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> addUser(@ModelAttribute UserDTO userDTO,
                                           @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(userService.addUser(userDTO, file), HttpStatus.CREATED);
    }

    /**
     * Updates a user with the given ID and userDTO, optionally including a file.
     *
     * @param id    the ID of the user to update
     * @param userDTO    the data transfer object representing the updated user
     * @param file    the file associated with the user
     * @return the ResponseEntity with the UserDTO object representing the updated user
     * @throws IOException if there is an error with the file operation
     */
    @PutMapping(value = "/updateUser/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @ModelAttribute UserDTO userDTO,
                                              @RequestParam(required = false) MultipartFile file) throws IOException {
        userDTO.setId(id);
        return ResponseEntity.ok(userService.update(id, userDTO, file));
    }

    /**
     * Deletes a user with the given ID.
     *
     * @param id the ID of the user to delete
     * @return a ResponseEntity object with no content, indicating a successful deletion
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the user ID is null
     */
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws IOException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}