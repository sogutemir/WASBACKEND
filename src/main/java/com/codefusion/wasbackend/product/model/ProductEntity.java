package com.codefusion.wasbackend.product.model;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.productField.model.ProductFieldEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ProductEntity extends BaseEntity {

    @NotBlank(message = "Name cannot be null")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Model cannot be null")
    @Column(name = "model")
    private String model;

    @Column(name = "current_stock")
    private int currentStock;

    @Column(name = "profit")
    private double profit;

    @Column(name = "product_code")
    private String productCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonBackReference
    private StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductFieldEntity> productFields;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TransactionEntity> transactions;

}
