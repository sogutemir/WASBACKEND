package com.codefusion.wasbackend.resourceFile.model;

import com.codefusion.wasbackend.product.model.ProductEntity;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.transaction.model.TransactionEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resource_file")
public class ResourceFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Lob
    @Column(name="resource_file_Data", columnDefinition="LONGBLOB")
    private byte[] data;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "resourceFile", fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToOne(mappedBy = "resourceFile", fetch = FetchType.LAZY)
    private StoreEntity store;

    @OneToOne(mappedBy = "resourceFile", fetch = FetchType.LAZY)
    private TransactionEntity transaction;

    @OneToOne(mappedBy = "resourceFile", fetch = FetchType.LAZY)
    private ProductEntity product;
}
