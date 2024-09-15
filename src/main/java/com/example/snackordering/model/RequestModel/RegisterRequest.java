package com.example.snackordering.model.RequestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Email(message = "has wrong format")
    private String email;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    private String password;
}
