package com.example.test.Repository;

import com.example.test.Entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUserId(String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c where c.cart.id = :cartId ")
    void deleteByCartId(@Param("cartId") String cartId);
}
