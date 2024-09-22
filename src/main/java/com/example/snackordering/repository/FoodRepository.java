package com.example.snackordering.repository;

import com.example.snackordering.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {

    Food findByFoodId(Integer foodId);

}
