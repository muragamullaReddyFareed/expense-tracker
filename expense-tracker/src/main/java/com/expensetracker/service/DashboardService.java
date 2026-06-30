package com.expensetracker.service;
import com.expensetracker.dto.DashboardResponse;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.security.AuthenticatedUserProvider;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
@Service
public class DashboardService {
    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserProvider userProvider;
    public DashboardService(TransactionRepository tr, AuthenticatedUserProvider up) {
        this.transactionRepository=tr; this.userProvider=up;
    }
    public DashboardResponse getSummary() {
        Long uid = userProvider.getCurrentUser().getId();
        BigDecimal income  = transactionRepository.sumAmountByUserIdAndType(uid, TransactionType.INCOME);
        BigDecimal expense = transactionRepository.sumAmountByUserIdAndType(uid, TransactionType.EXPENSE);
        List<DashboardResponse.CategoryBreakdown> incomeBreakdown = transactionRepository
                .getCategoryBreakdown(uid, TransactionType.INCOME).stream()
                .map(r -> new DashboardResponse.CategoryBreakdown(
                        (String)r[0], (BigDecimal)r[1], ((Number)r[2]).longValue())).toList();
        List<DashboardResponse.CategoryBreakdown> expenseBreakdown = transactionRepository
                .getCategoryBreakdown(uid, TransactionType.EXPENSE).stream()
                .map(r -> new DashboardResponse.CategoryBreakdown(
                        (String)r[0], (BigDecimal)r[1], ((Number)r[2]).longValue())).toList();
        return DashboardResponse.builder()
                .totalIncome(income).totalExpense(expense).savings(income.subtract(expense))
                .incomeByCategory(incomeBreakdown).expenseByCategory(expenseBreakdown).build();
    }
}