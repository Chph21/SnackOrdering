package com.example.snackordering.model.orderDetail;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {

    private Integer orderDetailId;

    @NotNull(message = "Order ID cannot be null")
    private Integer orderId;

    @NotNull(message = "BranchFood ID cannot be null")
    private Integer branchFoodId;

    @NotNull(message = "Quantity cannot be null")
    private int quantity;
}