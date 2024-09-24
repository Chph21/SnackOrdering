package com.example.snackordering.model.branchFood;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchFoodRequest {

    private Integer branchFoodId;

    @NotNull(message = "Quantity cannot be null")
    private String quantity;

    @NotNull(message = "Branch ID cannot be null")
    private Integer branchId;

    @NotNull(message = "Food ID cannot be null")
    private Integer foodId;
}