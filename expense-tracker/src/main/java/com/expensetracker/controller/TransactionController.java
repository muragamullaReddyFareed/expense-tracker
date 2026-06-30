package com.expensetracker.controller;
import com.expensetracker.dto.*;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) { this.transactionService=transactionService; }
    @PostMapping("/income")
    public ResponseEntity<TransactionResponse> addIncome(@Valid @RequestBody TransactionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addTransaction(req, TransactionType.INCOME));
    }
    @GetMapping("/income")
    public ResponseEntity<List<TransactionResponse>> getIncome(
            @RequestParam(required=false) String keyword,
            @RequestParam(required=false) String category,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.search(TransactionType.INCOME, keyword, category, startDate, endDate));
    }
    @PostMapping("/expenses")
    public ResponseEntity<TransactionResponse> addExpense(@Valid @RequestBody TransactionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addTransaction(req, TransactionType.EXPENSE));
    }
    @GetMapping("/expenses")
    public ResponseEntity<List<TransactionResponse>> getExpenses(
            @RequestParam(required=false) String keyword,
            @RequestParam(required=false) String category,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.search(TransactionType.EXPENSE, keyword, category, startDate, endDate));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id); return ResponseEntity.noContent().build();
    }
}