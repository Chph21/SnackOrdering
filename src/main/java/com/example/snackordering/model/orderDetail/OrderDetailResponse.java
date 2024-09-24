package com.example.snackordering.model.orderDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {

    private Integer orderDetailId;

    private Integer orderId;

    private Integer branchFoodId;

    private int quantity;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}
