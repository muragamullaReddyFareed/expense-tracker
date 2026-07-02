package com.expensetracker.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BudgetRequest {

    private String category; // null for overall monthly budget

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Month is required")
    @Min(1) @Max(12)
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(2000)
    private Integer year;

    public BudgetRequest() {}

    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public Integer getMonth() { return month; }
    public Integer getYear() { return year; }
    public void setCategory(String c) { this.category = c; }
    public void setAmount(BigDecimal a) { this.amount = a; }
    public void setMonth(Integer m) { this.month = m; }
    public void setYear(Integer y) { this.year = y; }
}