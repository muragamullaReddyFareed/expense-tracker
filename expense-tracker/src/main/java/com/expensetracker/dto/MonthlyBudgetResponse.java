package com.expensetracker.dto;

import java.math.BigDecimal;

public class MonthlyBudgetResponse {

    private Long id;
    private Integer month;
    private Integer year;
    private BigDecimal budgetAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;

    public MonthlyBudgetResponse() {}

    public MonthlyBudgetResponse(Long id, Integer month, Integer year,
                                 BigDecimal budgetAmount, BigDecimal spentAmount) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.budgetAmount = budgetAmount;
        this.spentAmount = spentAmount;
        this.remainingAmount = budgetAmount.subtract(spentAmount);
    }

    public Long getId() { return id; }
    public Integer getMonth() { return month; }
    public Integer getYear() { return year; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public BigDecimal getSpentAmount() { return spentAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }

    public void setId(Long id) { this.id = id; }
    public void setMonth(Integer m) { this.month = m; }
    public void setYear(Integer y) { this.year = y; }
    public void setBudgetAmount(BigDecimal b) { this.budgetAmount = b; }
    public void setSpentAmount(BigDecimal s) { this.spentAmount = s; }
    public void setRemainingAmount(BigDecimal r) { this.remainingAmount = r; }
}