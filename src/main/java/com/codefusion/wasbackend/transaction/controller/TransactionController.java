package com.codefusion.wasbackend.transaction.controller;

import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.transaction.dto.DailyTransactionTotalDTO;
import com.codefusion.wasbackend.transaction.dto.ReturnTransactionDTO;
import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.transaction.service.TransactionService;
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
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final ResourceFileService resourceFileService;


    /**
     * Retrieves a transaction by its ID.
     *
     * @param id the ID of the transaction to retrieve
     * @return the TransactionDTO object representing the transaction
     * @throws RuntimeException if the transaction is not found
     */
    @GetMapping("/getTransactionById/{id}")
    public ResponseEntity<ReturnTransactionDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    /**
     * Retrieves all transactions.
     *
     * @return a ResponseEntity object containing a list of TransactionDTO objects representing all transactions
     */
    @GetMapping("/allTransaction")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    /**
     * Retrieves all transactions associated with a specific store ID.
     *
     * @param productId the ID of the store
     * @return a list of TransactionDTO objects representing the transactions
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReturnTransactionDTO>> getTransactionsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(transactionService.getTransactionsByProductId(productId));
    }

    @GetMapping("/downloadResourceFile/{transactionId}")
    public ResponseEntity<byte[]> downloadTransactionResourceFile(@PathVariable Long transactionId) throws FileNotFoundException {

        TransactionEntity transactionEntity = transactionService.getTransactionEntityById(transactionId);
        Long fileId = resourceFileService.findResourceFileId(transactionEntity);

        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
        ResponseEntity.BodyBuilder responseBuilder = resourceFileService.retrieveResourceFile(fileId);
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getData());
    }

    @GetMapping("/daily-totals/{storeId}")
    public List<DailyTransactionTotalDTO> getDailyTransactionTotalsByStoreId(@PathVariable Long storeId) {
        return transactionService.getDailyTransactionTotalsByStoreId(storeId);
    }

    /**
     * Adds a new transaction.
     *
     * @param transactionDTO the data transfer object representing the transaction
     * @param file the file associated with the transaction
     * @return the data transfer object representing the added transaction
     * @throws IOException if there is an error with the file operation
     */
    @PostMapping(value = "/addTransaction", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TransactionDTO> addTransaction(@ModelAttribute TransactionDTO transactionDTO,
                                                         @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(transactionService.addTransaction(transactionDTO, file), HttpStatus.CREATED);
    }


//    /**
//     * Updates a transaction with the given ID, transaction DTO, and optional file.
//     *
//     * @param id              the ID of the transaction to update
//     * @param transactionDTO  the transaction DTO representing the updated transaction
//     * @param file            the file associated with the transaction (optional)
//     * @return the ResponseEntity containing the updated TransactionDTO
//     * @throws IOException    if there is an error with the file operation
//     */
//    @PutMapping(value = "/updateTransaction/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id,
//                                                            @ModelAttribute TransactionDTO transactionDTO,
//                                                            @RequestParam(required = false) MultipartFile file) throws IOException {
//        transactionDTO.setId(id);
//        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO, file));
//    }

    /**
     * Deletes a transaction entity with the given transaction ID.
     *
     * @param id the ID of the transaction entity to delete
     * @return a ResponseEntity object with a status code and no content
     * @throws IOException if there is an error with the file operation
     */
    @DeleteMapping("/deleteTransaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) throws IOException {
        transactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
