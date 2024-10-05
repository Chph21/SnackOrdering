package com.example.snackordering.model.shipments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentsRequest {

    private Integer shipmentId;

    @NotNull(message = "Order ID cannot be null")
    private Integer orderId;

    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    @NotNull(message = "Shipment date cannot be null")
    private Date shipmentDate;

    @NotNull(message = "Expected delivery date cannot be null")
    private Date expectedDeliveryDate;

    @NotNull(message = "Actual delivery date cannot be null")
    private Date actualDeliveryDate;

    @NotNull(message = "Shipping address cannot be null")
    @NotBlank(message = "Shipping address cannot be blank")
    private String shippingAddress;

    @NotNull(message = "Shipping cost cannot be null")
    @NotBlank(message = "Shipping cost cannot be blank")
    private String shippingCost;

    @NotNull(message = "Shipment status cannot be null")
    private int shipmentStatus;

    private String note;
}
