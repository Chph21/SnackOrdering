package com.example.snackordering.model.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {


    private Integer categoryId;

    private String description;

    private List<Integer> foodIds;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;
}
