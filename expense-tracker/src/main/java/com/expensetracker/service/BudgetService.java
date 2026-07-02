package com.expensetracker.service;

import com.expensetracker.dto.*;
import com.expensetracker.entity.Budget;
import com.expensetracker.entity.User;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.security.AuthenticatedUserProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserProvider userProvider;

    public BudgetService(BudgetRepository budgetRepository,
                         TransactionRepository transactionRepository,
                         AuthenticatedUserProvider userProvider) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.userProvider = userProvider;
    }

    // ── Set or update overall monthly budget ──────────────────────
    public MonthlyBudgetResponse setMonthlyBudget(BudgetRequest req) {
        User user = userProvider.getCurrentUser();

        // If budget already exists for this month/year → update it
        Budget budget = budgetRepository
                .findByUserIdAndMonthAndYearAndCategoryIsNull(
                        user.getId(), req.getMonth(), req.getYear())
                .orElse(Budget.builder()
                        .user(user)
                        .category(null)
                        .month(req.getMonth())
                        .year(req.getYear())
                        .build());

        budget.setAmount(req.getAmount());
        Budget saved = budgetRepository.save(budget);
        return toMonthlyResponse(saved);
    }

    // ── Get overall monthly budget with current spending ──────────
    public MonthlyBudgetResponse getMonthlyBudget(int month, int year) {
        User user = userProvider.getCurrentUser();

        Budget budget = budgetRepository
                .findByUserIdAndMonthAndYearAndCategoryIsNull(
                        user.getId(), month, year)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No budget set for this month"));

        return toMonthlyResponse(budget);
    }

    // ── Delete a budget ───────────────────────────────────────────
    public void delete(Long id) {
        Long uid = userProvider.getCurrentUser().getId();
        Budget budget = budgetRepository.findByIdAndUserId(id, uid)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));
        budgetRepository.delete(budget);
    }

    // ── Helper ────────────────────────────────────────────────────
    private MonthlyBudgetResponse toMonthlyResponse(Budget budget) {
        BigDecimal spent = transactionRepository.sumExpenseByUserAndMonth(
                budget.getUser().getId(),
                budget.getMonth(),
                budget.getYear());

        return new MonthlyBudgetResponse(
                budget.getId(),
                budget.getMonth(),
                budget.getYear(),
                budget.getAmount(),
                spent);
    }
}