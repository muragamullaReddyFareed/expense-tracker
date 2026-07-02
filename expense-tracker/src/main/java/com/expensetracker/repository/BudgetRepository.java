package com.expensetracker.repository;

import com.expensetracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Find overall monthly budget (category is NULL)
    Optional<Budget> findByUserIdAndMonthAndYearAndCategoryIsNull(
            Long userId, Integer month, Integer year);

    // Find budget by ID owned by user
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
}