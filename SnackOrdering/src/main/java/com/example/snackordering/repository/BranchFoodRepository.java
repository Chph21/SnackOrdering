package com.example.snackordering.repository;

import com.example.snackordering.entity.BranchFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchFoodRepository extends JpaRepository<BranchFood, Integer> {
}