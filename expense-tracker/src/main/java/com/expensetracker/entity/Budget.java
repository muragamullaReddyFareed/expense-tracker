package com.expensetracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Budget() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getCategory() { return category; }
    public Integer getMonth() { return month; }
    public Integer getYear() { return year; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User u) { this.user = u; }
    public void setCategory(String c) { this.category = c; }
    public void setMonth(Integer m) { this.month = m; }
    public void setYear(Integer y) { this.year = y; }
    public void setAmount(BigDecimal a) { this.amount = a; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id;
        private User user;
        private String category;
        private Integer month, year;
        private BigDecimal amount;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User u) { this.user = u; return this; }
        public Builder category(String c) { this.category = c; return this; }
        public Builder month(Integer m) { this.month = m; return this; }
        public Builder year(Integer y) { this.year = y; return this; }
        public Builder amount(BigDecimal a) { this.amount = a; return this; }
        public Budget build() {
            Budget b = new Budget();
            b.id = id; b.user = user; b.category = category;
            b.month = month; b.year = year; b.amount = amount;
            return b;
        }
    }
}