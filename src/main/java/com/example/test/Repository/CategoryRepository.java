package com.example.test.Repository;

import com.example.test.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    boolean existsByName(String name);

    List<Category> findAll();

    @Query("select c from Category c where LOWER(c.name) = LOWER(:name)")
    Optional<Category> findByCategoryName(@Param("name") String name);

}
