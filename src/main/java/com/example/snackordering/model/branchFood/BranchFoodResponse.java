package com.example.snackordering.model.branchFood;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchFoodResponse {

    private Integer branchFoodId;

    private String quantity;

    private Integer branchId;

    private Integer foodId;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}