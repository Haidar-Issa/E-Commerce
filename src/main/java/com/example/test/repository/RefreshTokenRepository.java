package com.example.test.repository;

import com.example.test.entity.RefreshToken;
import com.example.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional <RefreshToken> findByToken(String userId);

    void deleteByUser(User user);

    boolean existsByUser(User user);

}
