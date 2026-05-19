package com.example.test.Repository;

import com.example.test.Entity.RefreshToken;
import com.example.test.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional <RefreshToken> findByToken(String userId);

    void deleteByUser(User user);

    boolean existsByUser(User user);

}
