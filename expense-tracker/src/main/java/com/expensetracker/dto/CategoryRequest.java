package com.expensetracker.dto;
import com.expensetracker.entity.TransactionType;
import jakarta.validation.constraints.*;
public class CategoryRequest {
    @NotBlank(message = "Category name is required") private String name;
    @NotNull(message = "Type is required") private TransactionType type;
    public CategoryRequest() {}
    public String getName() { return name; }
    public TransactionType getType() { return type; }
    public void setName(String name) { this.name = name; }
    public void setType(TransactionType type) { this.type = type; }
}