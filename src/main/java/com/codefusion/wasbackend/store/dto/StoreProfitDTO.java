package com.codefusion.wasbackend.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StoreProfitDTO {
    private String name;
    private double totalProfit;
}