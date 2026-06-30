package com.expensetracker.repository;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdOrderByNameAsc(Long userId);
    List<Category> findByUserIdAndTypeOrderByNameAsc(Long userId, TransactionType type);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}