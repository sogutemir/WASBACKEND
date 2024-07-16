package com.codefusion.wasbackend.Category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategoryProfitDTO {
    private String categoryName;
    private double totalProfit;
}