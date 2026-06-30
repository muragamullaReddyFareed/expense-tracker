package com.expensetracker.dto;
import com.expensetracker.entity.TransactionType;
public class CategoryResponse {
    private Long id; private String name; private TransactionType type;
    public CategoryResponse() {}
    public CategoryResponse(Long id, String name, TransactionType type) {
        this.id=id; this.name=name; this.type=type;
    }
    public Long getId(){return id;}
    public String getName(){return name;}
    public TransactionType getType(){return type;}
    public void setId(Long id){this.id=id;}
    public void setName(String n){this.name=n;}
    public void setType(TransactionType t){this.type=t;}
}