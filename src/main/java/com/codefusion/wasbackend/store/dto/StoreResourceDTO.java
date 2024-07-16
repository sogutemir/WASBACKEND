package com.codefusion.wasbackend.store.dto;

import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreResourceDTO {

    private StoreDTO storeDTO;
    private ResourceFileDTO resourceFileDTO;

}