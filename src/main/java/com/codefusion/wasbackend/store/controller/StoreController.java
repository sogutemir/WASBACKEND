package com.codefusion.wasbackend.store.controller;

import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.store.dto.ReturnStoreDTO;
import com.codefusion.wasbackend.store.dto.StoreProfitDTO;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.store.service.StoreService;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final ResourceFileService resourceFileService;


    /**
     * Retrieves a StoreDTO object by its ID.
     *
     * @param id the ID of the store
     * @return the StoreDTO object representing the retrieved store
     * @throws RuntimeException if the store is not found
     */
    @GetMapping("/getStoreById/{id}")
    public ResponseEntity<ReturnStoreDTO> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    /**
     * Downloads a resource file for a user.
     *
     * @param storeId the ID of the store for which the resource file is being downloaded
     * @return a ResponseEntity object containing the resource file data
     * @throws FileNotFoundException if the resource file is not found
     */
    @GetMapping("/downloadResourceFile/{storeId}")
    public ResponseEntity<byte[]> downloadStoreResourceFile(@PathVariable Long storeId) throws FileNotFoundException {
        StoreEntity storeEntity = storeService.getStoreEntityById(storeId);
        Long fileId = resourceFileService.findResourceFileId(storeEntity);
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
        ResponseEntity.BodyBuilder responseBuilder = resourceFileService.retrieveResourceFile(fileId);
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getData());
    }

    /**
     * Retrieves a list of all stores.
     *
     * @return a ResponseEntity object containing a list of StoreDTO representing all stores
     */
    @GetMapping("/allStore")
    public ResponseEntity<List<ReturnStoreDTO>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/top3StoresByProfit/{userId}")
    public ResponseEntity<List<StoreProfitDTO>> getTop3StoresByProfitForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(storeService.getTop3StoresByProfitForUser(userId));
    }

    /**
     * Retrieves a list of stores based on the store ID.
     *
     * @param userId the ID of the store
     * @return a ResponseEntity containing a list of StoreDTO objects representing the stores associated with the specified store ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<ReturnStoreDTO>> getStoresByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(storeService.getStoresByUserId(userId));
    }

    /**
     * Retrieves the top 5 most profitable products of a specific store.
     *
     * @param storeId the ID of the store
     * @return a ResponseEntity containing a list of the top 5 most profitable ProductDto objects
     */
    @GetMapping("/{storeId}/top5MostProfitableProducts")
    public ResponseEntity<List<ReturnStoreDTO.ProductDto>> getTop5MostProfitableProducts(
            @PathVariable Long storeId,
            @RequestParam(required = false, defaultValue = "true") boolean top) {
        List<ReturnStoreDTO.ProductDto> products = storeService.getTop5MostProfitableProducts(storeId, top);
        return ResponseEntity.ok(products);
    }



    /**
     * Adds a new store to the system.
     *
     * @param storeDTO the data transfer object representing the store
     * @param file the file associated with the store
     * @return the data transfer object representing the added store
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the storeDTO is null
     */
    @PostMapping(value = "/addStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreDTO> addStore(@ModelAttribute StoreDTO storeDTO,
                                             @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(storeService.addStore(storeDTO, file), HttpStatus.CREATED);
    }


    /**
     * Updates a store with the specified ID, using the provided storeDTO and an optional file.
     *
     * @param id   the ID of the store to update
     * @param storeDTO   the StoreDTO object representing the updated store
     * @param file   an optional MultipartFile object associated with the store
     * @return a ResponseEntity containing the updated StoreDTO object
     * @throws IOException if there is an error with the file operation
     */
    @PutMapping(value = "/updateStore/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreDTO> updateStore(@PathVariable Long id,
                                                @ModelAttribute StoreDTO storeDTO,
                                                @RequestParam(required = false) MultipartFile file) throws IOException {
        storeDTO.setId(id);
        return ResponseEntity.ok(storeService.update(id, storeDTO, file));
    }

    /**
     * Deletes a store with the specified ID.
     *
     * @param id the ID of the store to delete
     * @return a ResponseEntity with no content if successful
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the store ID is null
     */
    @DeleteMapping("/deleteStore/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) throws IOException {
        storeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
