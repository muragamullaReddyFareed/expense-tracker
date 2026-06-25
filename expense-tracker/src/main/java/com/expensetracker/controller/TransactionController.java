package com.expensetracker.controller;

import com.expensetracker.dto.TransactionRequest;
import com.expensetracker.dto.TransactionResponse;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/income")
    public ResponseEntity<TransactionResponse> addIncome(
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.addTransaction(
                        request, TransactionType.INCOME));
    }

    @GetMapping("/income")
    public ResponseEntity<List<TransactionResponse>> getIncome() {
        return ResponseEntity.ok(
                transactionService.getByType(TransactionType.INCOME));
    }

    @PostMapping("/expenses")
    public ResponseEntity<TransactionResponse> addExpense(
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.addTransaction(
                        request, TransactionType.EXPENSE));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<TransactionResponse>> getExpenses() {
        return ResponseEntity.ok(
                transactionService.getByType(TransactionType.EXPENSE));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}