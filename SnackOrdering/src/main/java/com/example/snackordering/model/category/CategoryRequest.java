package com.example.snackordering.model.category;

import com.example.snackordering.entity.Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    private Integer categoryId;

    private String description;

    private List<Food> foodIds;
}
