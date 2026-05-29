package com.example.test.Repository;

import com.example.test.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    @Query("select u from User u")
    Page<User> findSomeUsers(Pageable pageable);

    @Query("select u.firstName , u.lastName from User u where u.role=:role")
    Page<User> findUsersByRole(@Param("role") String role,
                                     Pageable pageable);

    @Query("select u from User u where u.email = :email")
    Page<User> findUsersByEmail(@Param("email") String email, Pageable pageable);

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    boolean existsUserById(String id);

    Page<User> findUsersByActive(boolean active , Pageable pageable);

    Optional<User> findById(String id);

}
