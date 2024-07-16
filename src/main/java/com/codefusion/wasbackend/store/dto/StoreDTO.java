package com.codefusion.wasbackend.store.dto;

import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.product.model.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StoreDTO implements Serializable {
    Long id;

    @NotBlank(message = "Name cannot be empty")
    String name;

    @NotBlank(message = "Address cannot be empty")
    String address;

    String description;

    String storePhoneNo;

    Long resourceFileId;

    @NotNull
    List<Long> userIds;

    List<ProductEntity> products;

    Long companyId;
}