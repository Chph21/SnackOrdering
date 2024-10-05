package com.example.snackordering.model.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private Integer orderId;

    @NotNull(message = "Order number cannot be null")
    @NotBlank(message = "Order number cannot be blank")
    private String orderNumber;

    @NotNull(message = "TotalMoney number cannot be null")
    private double totalMoney;

    @NotNull(message = "Status number cannot be null")
    @NotBlank(message = "Status number cannot be blank")
    private String status;

    @NotNull(message = "AccountId number cannot be null")
    @NotBlank(message = "AccountId number cannot be blank")
    private Long accountId;

    @NotNull(message = "BranchId number cannot be null")
    @NotBlank(message = "BranchId number cannot be blank")
    private Integer branchId;

}