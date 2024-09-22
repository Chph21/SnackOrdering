package com.example.snackordering.model.category;
import com.example.snackordering.entity.Food;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
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
