package com.expensetracker.service;

import com.expensetracker.dto.DashboardResponse;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.entity.User;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public DashboardResponse getSummary() {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        BigDecimal totalIncome = transactionRepository
                .sumAmountByUserIdAndType(
                        currentUser.getId(), TransactionType.INCOME);

        BigDecimal totalExpense = transactionRepository
                .sumAmountByUserIdAndType(
                        currentUser.getId(), TransactionType.EXPENSE);

        BigDecimal savings = totalIncome.subtract(totalExpense);

        return DashboardResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .savings(savings)
                .build();
    }
}