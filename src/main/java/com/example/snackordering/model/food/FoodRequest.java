package com.example.snackordering.model.food;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequest {

    private Integer foodId;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String foodName;

    @NotNull(message = "Image cannot be null")
    @NotBlank(message = "Image cannot be blank")
    private String imageURL;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Ingredients cannot be null")
    @NotBlank(message = "Ingredients cannot be blank")
    private String ingredients;

    @NotNull(message = "Price cannot be null")
    private double price;

    @NotNull(message = "Availability cannot be null")
    private boolean isAvailable;

    private Integer categoryId;
}
