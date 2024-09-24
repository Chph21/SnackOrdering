package com.example.snackordering.repository;

import com.example.snackordering.entity.Shipments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentsRepository extends JpaRepository<Shipments, Integer> {
}
