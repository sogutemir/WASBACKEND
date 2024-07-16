package com.codefusion.wasbackend.CategoryPrototype.model;

import com.codefusion.wasbackend.Category.model.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_prototype")
public class CategoryPrototypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private CategoryEntity category;

    @PrePersist
    void prePersist() {
        this.isDelete = false;
    }
}
