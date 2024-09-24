package com.example.snackordering.model.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRequest {

    private Integer branchId;

    @NotNull(message = "Branch name cannot be null")
    @NotBlank(message = "Branch name cannot be blank")
    private String branchName;

    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull(message = "Phone cannot be null")
    @NotBlank(message = "Phone cannot be blank")
    private String phone;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Image cannot be null")
    @NotBlank(message = "Image cannot be blank")
    private String image;

    @NotNull(message = "Manager cannot be null")
    @NotBlank(message = "Manager cannot be blank")
    private String manager;

    @NotNull(message = "Manager phone cannot be null")
    @NotBlank(message = "Manager phone cannot be blank")
    private String managerPhone;

    @NotNull(message = "Manager email cannot be null")
    @NotBlank(message = "Manager email cannot be blank")
    private String managerEmail;

    @NotNull(message = "Manager address cannot be null")
    @NotBlank(message = "Manager address cannot be blank")
    private String managerAddress;

    @NotNull(message = "Manager image cannot be null")
    @NotBlank(message = "Manager image cannot be blank")
    private String managerImage;

    @NotNull(message = "Manager description cannot be null")
    @NotBlank(message = "Manager description cannot be blank")
    private String managerDescription;

    @NotNull(message = "Manager status cannot be null")
    @NotBlank(message = "Manager status cannot be blank")
    private String managerStatus;

    @NotNull(message = "Manager opening hour cannot be null")
    @NotBlank(message = "Manager opening hour cannot be blank")
    private String managerOpeningHour;

    @NotNull(message = "AccountId opening hour cannot be null")
    @NotBlank(message = "AccountId opening hour cannot be blank")
    private Long accountId;

    @NotNull(message = "Status cannot be null")
    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotNull(message = "Opening hour cannot be null")
    @NotBlank(message = "Opening hour cannot be blank")
    private String openingHour;

    @NotNull(message = "Latitude cannot be null")
    private double latitude;

    @NotNull(message = "Longitude cannot be null")
    private double longitude;
}