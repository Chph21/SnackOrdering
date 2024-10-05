package com.example.snackordering.model.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {

    private Integer branchId;

    private String branchName;

    private String address;

    private String phone;

    private String email;

    private String description;

    private String image;

    private String manager;

    private String managerPhone;

    private String managerEmail;

    private String managerAddress;

    private String managerImage;

    private String managerDescription;

    private String managerStatus;

    private String managerOpeningHour;

    private String status;

    private String openingHour;

    private double latitude;

    private double longitude;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}