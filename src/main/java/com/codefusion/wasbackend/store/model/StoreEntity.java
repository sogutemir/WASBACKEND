package com.codefusion.wasbackend.store.model;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class StoreEntity extends BaseEntity {

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotBlank(message = "Address cannot be empty")
    @Column(name = "address")
    private String address;

    @Column(name = "store_no")
    private String storePhoneNo;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_stores",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductEntity> products;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}

