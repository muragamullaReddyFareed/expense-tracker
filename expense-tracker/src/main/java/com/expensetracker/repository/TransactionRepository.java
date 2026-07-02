package com.expensetracker.repository;
import com.expensetracker.entity.Transaction;
import com.expensetracker.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdAndTypeOrderByTransactionDateDesc(Long userId, TransactionType type);
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type);
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type = :type " +
            "AND (:keyword IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(t.category) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
            "AND (:category IS NULL OR t.category = :category) " +
            "AND (:startDate IS NULL OR t.transactionDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.transactionDate <= :endDate) " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> searchTransactions(
            @Param("userId") Long userId, @Param("type") TransactionType type,
            @Param("keyword") String keyword, @Param("category") String category,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT YEAR(t.transactionDate), MONTH(t.transactionDate), SUM(t.amount), COUNT(t) " +
            "FROM Transaction t WHERE t.user.id = :userId AND t.type = :type " +
            "AND YEAR(t.transactionDate) = :year " +
            "GROUP BY YEAR(t.transactionDate), MONTH(t.transactionDate) ORDER BY MONTH(t.transactionDate) ASC")
    List<Object[]> getMonthlyReport(@Param("userId") Long userId, @Param("type") TransactionType type, @Param("year") int year);
    @Query("SELECT t.category, SUM(t.amount), COUNT(t) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type GROUP BY t.category ORDER BY SUM(t.amount) DESC")
    List<Object[]> getCategoryBreakdown(@Param("userId") Long userId, @Param("type") TransactionType type);
    @Query("SELECT DISTINCT YEAR(t.transactionDate) FROM Transaction t WHERE t.user.id = :userId ORDER BY YEAR(t.transactionDate) DESC")
    List<Integer> getAvailableYears(@Param("userId") Long userId);
    // Total expense spent in a given month/year
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = 'EXPENSE' " +
            "AND YEAR(t.transactionDate) = :year " +
            "AND MONTH(t.transactionDate) = :month")
    java.math.BigDecimal sumExpenseByUserAndMonth(
            @Param("userId") Long userId,
            @Param("month") int month,
            @Param("year") int year);
}