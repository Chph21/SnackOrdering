package com.example.snackordering.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long accountId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String address;

    private Long branchId;

    private LocalDate birthday;

    private String createdBy;

    private LocalDateTime createdDate;

    private String updatedBy;

    private LocalDateTime updatedDate;

    private double latitude;

    private double longitude;
}
