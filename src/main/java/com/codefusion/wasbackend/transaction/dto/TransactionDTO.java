package com.codefusion.wasbackend.transaction.dto;

import com.codefusion.wasbackend.product.model.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.codefusion.wasbackend.transaction.model.TransactionEntity}
 */
@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO implements Serializable {
    Long id;
    Boolean isBuying;

    @PastOrPresent(message = "Transaction date must be today or in the past")
    @DateTimeFormat(pattern="MM-dd-yyyy")
    LocalDate date;
    int quantity;
    Double price;
    @NotBlank(message = "Full name cannot be empty")
    String fullName;
    @NotBlank(message = "Address cannot be empty")
    String address;
    @Size(min = 9, max = 13)
    @NotBlank(message = "Phone cannot be empty")
    String phone;
    ProductEntity product;

    Long resourceFileId;
}