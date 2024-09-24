package com.example.snackordering.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Integer orderId;

    private String orderNumber;

    private double totalMoney;

    private String status;

    private Long accountId;

    private Integer branchId;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}
