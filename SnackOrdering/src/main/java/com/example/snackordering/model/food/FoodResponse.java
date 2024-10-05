package com.example.snackordering.model.food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {


    private Integer foodId;

    private String foodName;

    private String imageURL;

    private String description;

    private String ingredients;

    private double price;

    private boolean isAvailable;

    private Integer categoryId;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}
