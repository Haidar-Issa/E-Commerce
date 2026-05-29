package com.example.test.Repository;

import com.example.test.Entity.Category;
import com.example.test.Entity.Product;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Product findByName(String name);

    boolean existsByName(String name);

    //    find by Category Name
    @Query("Select p From Product p LEFT JOIN p.category c where LOWER(c.name) = LOWER(:categoryName)")
    Page<Product> findByCategoryName(
            @Param("categoryName") String categoryName,
            Pageable pageable);

//    @Query(""" SELECT p FROM Product p JOIN p.category c WHERE\s
//            (:min_price IS NULL OR p.price >= :min_price) AND
//            (:max_price IS NULL OR p.price <= :max_price) AND\s
//                           (:
//            categoryName IS NULL OR LOWER(c.name) = LOWER(:categoryName)) """)
//    Page<Product> findProductByPriceAndCategoryName(
//            @Param("min_price") BigDecimal min_price,
//            @Param("max_price") BigDecimal max_price,
//            @Param("categoryName") String categoryName,
//            Pageable pageable);

    Page<Product>findByCategoryAndPriceBetween(Category category, @DecimalMin(value = "0.0", inclusive = false) BigDecimal price, @DecimalMin(value = "0.0", inclusive = false) BigDecimal price2, Pageable pageable);

    Optional<Product> findById(String id);

    List<Category> getByCategory(Category category);
}
