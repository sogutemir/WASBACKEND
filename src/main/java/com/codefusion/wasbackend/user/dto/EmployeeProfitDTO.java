package com.codefusion.wasbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmployeeProfitDTO {
    private String name;
    private String surname;
    private double totalProfit;
}