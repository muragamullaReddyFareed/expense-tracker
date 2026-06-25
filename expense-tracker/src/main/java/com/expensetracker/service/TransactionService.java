package com.expensetracker.service;

import com.expensetracker.dto.TransactionRequest;
import com.expensetracker.dto.TransactionResponse;
import com.expensetracker.entity.Transaction;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.entity.User;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public TransactionResponse addTransaction(
            TransactionRequest request, TransactionType type) {

        User currentUser = authenticatedUserProvider.getCurrentUser();

        Transaction transaction = Transaction.builder()
                .user(currentUser)
                .type(type)
                .category(request.getCategory())
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    public List<TransactionResponse> getByType(TransactionType type) {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        return transactionRepository
                .findByUserIdAndTypeOrderByTransactionDateDesc(
                        currentUser.getId(), type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TransactionResponse> getAll() {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        return transactionRepository
                .findByUserIdOrderByTransactionDateDesc(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long transactionId) {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        Transaction transaction = transactionRepository
                .findByIdAndUserId(transactionId, currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .type(t.getType())
                .category(t.getCategory())
                .amount(t.getAmount())
                .description(t.getDescription())
                .transactionDate(t.getTransactionDate())
                .build();
    }
}