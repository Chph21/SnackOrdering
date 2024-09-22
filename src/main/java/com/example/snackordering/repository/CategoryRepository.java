package com.example.snackordering.repository;

import com.example.snackordering.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByCategoryId(Integer categoryId);

}
