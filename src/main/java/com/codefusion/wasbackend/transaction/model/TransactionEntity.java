package com.codefusion.wasbackend.transaction.model;

import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity extends BaseEntity {

    @Column(name = "is_buying")
    private Boolean isBuying;

    @PastOrPresent(message = "Transaction date must be today or in the past")
    @Column(name = "date")
    private LocalDate date;

    @NotNull(message = "Price cannot be empty")
    @Column(name = "price")
    private Double price;

    @NotBlank(message = "Full name cannot be empty")
    @Column(name = "full_name")
    private String fullName;

    @NotNull(message = "Quantity cannot be empty")
    @Column(name = "quantity")
    private int quantity;

    @NotBlank(message = "Address cannot be empty")
    @Column(name = "address")
    private String address;

    @Size(min=9, max=13)
    @NotBlank(message = "Phone cannot be empty")
    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private ProductEntity product;

}
