package com.expensetracker.service;
import com.expensetracker.dto.*;
import com.expensetracker.entity.*;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.security.AuthenticatedUserProvider;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserProvider userProvider;
    public TransactionService(TransactionRepository tr, AuthenticatedUserProvider up) {
        this.transactionRepository=tr; this.userProvider=up;
    }
    public TransactionResponse addTransaction(TransactionRequest req, TransactionType type) {
        User u = userProvider.getCurrentUser();
        Transaction t = Transaction.builder().user(u).type(type).category(req.getCategory())
                .amount(req.getAmount()).description(req.getDescription())
                .transactionDate(req.getTransactionDate()).build();
        return toResponse(transactionRepository.save(t));
    }
    public List<TransactionResponse> search(TransactionType type, String keyword,
                                            String category, LocalDate startDate, LocalDate endDate) {
        Long uid = userProvider.getCurrentUser().getId();
        String kw  = (keyword  != null && !keyword.isBlank())  ? keyword.trim()  : null;
        String cat = (category != null && !category.isBlank() && !category.equals("ALL")) ? category : null;
        return transactionRepository.searchTransactions(uid, type, kw, cat, startDate, endDate)
                .stream().map(this::toResponse).toList();
    }
    public void delete(Long id) {
        User u = userProvider.getCurrentUser();
        Transaction t = transactionRepository.findByIdAndUserId(id, u.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(t);
    }
    public TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder().id(t.getId()).type(t.getType())
                .category(t.getCategory()).amount(t.getAmount())
                .description(t.getDescription()).transactionDate(t.getTransactionDate()).build();
    }
}