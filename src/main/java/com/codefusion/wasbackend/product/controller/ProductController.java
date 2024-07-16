package com.codefusion.wasbackend.product.controller;

import com.codefusion.wasbackend.product.dto.ReturnProductDTO;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.product.service.ProductService;
import com.codefusion.wasbackend.product.dto.ProductDTO;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
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
@RequestMapping("/product")
public class ProductController {
    
    private final ProductService productService;
    private final ResourceFileService resourceFileService;

    /**
     * Retrieves a {@link ProductDTO} by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the {@link ResponseEntity} with the {@link ProductDTO} corresponding to the ID
     */
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ReturnProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/downloadResourceFile/{productId}")
    public ResponseEntity<byte[]> downloadUserResourceFile(@PathVariable Long productId) throws FileNotFoundException {
        ProductEntity productEntity = productService.getProductEntityById(productId);
        Long fileId = resourceFileService.findResourceFileId(productEntity);
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
        ResponseEntity.BodyBuilder responseBuilder = resourceFileService.retrieveResourceFile(fileId);
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getData());
    }

    /**
     * Retrieves all products.
     *
     * @return a list of {@link ProductDTO} objects representing the products.
     */
    @GetMapping("/allProduct")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Retrieves the list of products for a given store ID.
     *
     * @param storeId the ID of the store
     * @return the list of products corresponding to the store
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ReturnProductDTO>> getProductsByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(productService.getProductsByStoreId(storeId));
    }
    /**
     * Adds a new product to the system.
     *
     * @param productDTO the {@link ProductDTO} object representing the product to be added
     * @param file the {@link MultipartFile} object representing the uploaded file
     * @return the {@link ResponseEntity} object containing the {@link ProductDTO} representing the added product
     * @throws IOException if there is an error with the file operation
     */
    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReturnProductDTO> addProduct(@ModelAttribute ProductDTO productDTO,
                                                       @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(productService.addProduct(productDTO, file), HttpStatus.CREATED);
    }


//    @PostMapping(value = "/addProductID", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Long> addProductID(@ModelAttribute ProductDTO productDTO, @RequestParam("file") MultipartFile file) throws IOException {
//        return new ResponseEntity<>(productService.addProductID(productDTO, file), HttpStatus.CREATED);
//    }

    /**
     * Updates a product with the given ID, product DTO, and optional file.
     *
     * @param id the ID of the product to update
     * @param productDTO the product DTO representing the updated product
     * @param file the optional file associated with the product
     * @return the response entity with the updated productDTO
     * @throws IOException if there is an error with the file operation
     */
    @PutMapping(value = "/updateProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReturnProductDTO> updateProduct(@PathVariable Long id,
                                                          @ModelAttribute ProductDTO productDTO,
                                                          @RequestParam(required = false) MultipartFile file) throws IOException {
        if (productDTO == null) {
            throw new IllegalArgumentException("ProductDTO cannot be null");
        }

        productDTO.setId(id);
        ReturnProductDTO updatedProduct = productService.updateProduct(id, productDTO, file);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deletes a product with the given product ID.
     *
     * @param id the ID of the product to delete
     * @return a ResponseEntity with no content
     * @throws IOException if there is an error with the file operation
     * @throws NullPointerException if the product ID is null
     */
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
