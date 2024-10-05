package com.example.snackordering.model.shipments;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentsResponse {

    private Integer shipmentId;

    private Integer orderId;

    private Long accountId;

    private Date shipmentDate;

    private Date expectedDeliveryDate;

    private Date actualDeliveryDate;

    private String shippingAddress;

    private String shippingCost;

    private int shipmentStatus;

    private String note;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}
