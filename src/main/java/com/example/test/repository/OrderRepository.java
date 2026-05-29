package com.example.test.Repository;

import com.example.test.Entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {

    Optional<Order> findById(String id);
    boolean existsById(String id);

    Optional<Page<Order>> findByUserId(String userId , Pageable pageable);
    boolean existsByUserId(String userId);



}
