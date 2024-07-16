package com.codefusion.wasbackend.productField.controller;

import com.codefusion.wasbackend.productField.dto.ProductFieldSaveDTO;
import com.codefusion.wasbackend.productField.dto.ProductFieldDTO;
import com.codefusion.wasbackend.productField.service.ProductFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productField")
@RequiredArgsConstructor
public class ProductFieldController {

    /**
     *
     */
    private final ProductFieldService productFieldService;

    /**
     * Retrieves the {@link ProductFieldDTO} associated with the given fieldId.
     *
     * @param id the ID of the product field to retrieve
     * @return the {@link ProductFieldDTO} associated with the given fieldId
     * @throws IllegalArgumentException if no product field is found with the given fieldId
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductFieldDTO> getProductFieldById(@PathVariable Long id) {
        return new ResponseEntity<>(productFieldService.getProductFieldById(id), HttpStatus.OK);
    }

    /**
     * Retrieves all product fields.
     *
     * @return a list of ProductFieldDTO representing all product fields
     */
    @GetMapping("/getAllProductField")
    public ResponseEntity<List<ProductFieldDTO>> getAllProductFields() {
        return new ResponseEntity<>(productFieldService.getAllProductField(), HttpStatus.OK);
    }

    /**
     * Retrieves the list of {@link ProductFieldDTO} objects associated with a given productId.
     *
     * @param productId the ID of the product
     * @return the list of ProductFieldDTO objects associated with the given productId
     * @throws IllegalArgumentException if productId is null
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductFieldDTO>> getProductFieldsByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(productFieldService.getProductFieldByProductId(productId), HttpStatus.OK);
    }

    /**
     * Adds a new product field.
     *
     * @param productFieldDTO the DTO representing the product field to be added (must not be null)
     * @return the DTO representing the added product field
     * @throws IllegalArgumentException if the productFieldDTO is null
     */
    @PostMapping("/addProductField")
    public ResponseEntity<ProductFieldDTO> addProductField(@RequestBody ProductFieldDTO productFieldDTO) {
        return new ResponseEntity<>(productFieldService.addProductField(productFieldDTO), HttpStatus.CREATED);
    }

    @PostMapping("/addProductFields/{productID}")
    public ResponseEntity<List<ProductFieldDTO>> addProductFields(@RequestBody List<ProductFieldSaveDTO> productFieldSaveDTOs, @PathVariable Long productID) {
        return new ResponseEntity<>(productFieldService.addProductFields(productFieldSaveDTOs, productID), HttpStatus.CREATED);
    }

    /**
     * Updates a product field with the specified ID.
     *
     * @param id              The ID of the product field to update.
     * @param productFieldDTO The updated product field data.
     * @return The updated product field.
     * @throws IllegalArgumentException if the ID or product field DTO is null.
     */
    @PutMapping("/updateProductField/{id}")
    public ResponseEntity<List<ProductFieldDTO>> updateProductField(@PathVariable Long id, @RequestBody List<ProductFieldSaveDTO> productFieldDTO) {
        return new ResponseEntity<>(productFieldService.updateProductField(id, productFieldDTO), HttpStatus.OK);
    }

    /**
     * Deletes a product field by its ID.
     *
     * @param id the ID of the product field to be deleted
     * @return a ResponseEntity with a status code of NO_CONTENT if the deletion is successful
     * @throws IllegalArgumentException if the ID is null
     * @throws Exception                if an error occurs while deleting the product field
     */
    @DeleteMapping("/deleteProductField/{id}")
    public ResponseEntity<?> deleteProductField(@PathVariable Long id) {
        productFieldService.deleteProductField(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}