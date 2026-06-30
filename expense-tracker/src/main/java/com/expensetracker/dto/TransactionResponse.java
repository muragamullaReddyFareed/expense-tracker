package com.expensetracker.dto;

import com.expensetracker.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private String category;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;

    public TransactionResponse() {}

    public Long getId() { return id; }
    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setId(Long id) { this.id = id; }
    public void setType(TransactionType type) { this.type = type; }
    public void setCategory(String category) { this.category = category; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private TransactionType type; private String category;
        private BigDecimal amount; private String description; private LocalDate transactionDate;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder type(TransactionType type) { this.type = type; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder transactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; return this; }
        public TransactionResponse build() {
            TransactionResponse r = new TransactionResponse();
            r.id = id; r.type = type; r.category = category;
            r.amount = amount; r.description = description; r.transactionDate = transactionDate;
            return r;
        }
    }
}