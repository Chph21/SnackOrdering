package com.example.snackordering.repository;

import com.example.snackordering.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, String> {
    Optional<AuthEntity> findByEmail(String email);
    Optional<AuthEntity> findByRefreshToken(String refreshToken);
}
