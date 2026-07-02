package com.expensetracker.controller;

import com.expensetracker.dto.*;
import com.expensetracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // Set or update overall monthly budget
    @PostMapping("/monthly")
    public ResponseEntity<MonthlyBudgetResponse> setMonthly(
            @Valid @RequestBody BudgetRequest req) {
        return ResponseEntity.ok(budgetService.setMonthlyBudget(req));
    }

    // Get overall monthly budget with current spending
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyBudgetResponse> getMonthly(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        LocalDate now = LocalDate.now();
        int m = (month != null) ? month : now.getMonthValue();
        int y = (year  != null) ? year  : now.getYear();
        return ResponseEntity.ok(budgetService.getMonthlyBudget(m, y));
    }

    // Delete a budget
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}