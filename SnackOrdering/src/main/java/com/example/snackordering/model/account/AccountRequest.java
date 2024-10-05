package com.example.snackordering.model.account;

import com.example.snackordering.enums.AccountRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private Long accountId;

    @NotNull(message = "Firstname cannot be null")
    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;

    @NotNull(message = "Lastname cannot be null")
    @NotBlank(message = "Lastname cannot be blank")
    private String lastName;

    private String email;

    @NotNull(message = "Phone cannot be null")
    @NotBlank(message = "Phone cannot be blank")
    private String phone;

    private String address;

    private Integer branchId;

    private LocalDate birthday;

}
