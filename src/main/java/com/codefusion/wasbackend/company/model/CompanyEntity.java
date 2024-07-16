package com.codefusion.wasbackend.company.model;

import com.codefusion.wasbackend.base.model.BaseEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class CompanyEntity extends BaseEntity {

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "tax_level")
    private String taxLevel;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreEntity> stores;

    @OneToOne(mappedBy = "company")
    private UserEntity user;
}
