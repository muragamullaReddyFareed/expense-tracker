package com.expensetracker.dto;
import java.math.BigDecimal;
import java.util.List;
public class DashboardResponse {
    private BigDecimal totalIncome; private BigDecimal totalExpense; private BigDecimal savings;
    private List<CategoryBreakdown> incomeByCategory; private List<CategoryBreakdown> expenseByCategory;
    public DashboardResponse() {}
    public BigDecimal getTotalIncome(){return totalIncome;}
    public BigDecimal getTotalExpense(){return totalExpense;}
    public BigDecimal getSavings(){return savings;}
    public List<CategoryBreakdown> getIncomeByCategory(){return incomeByCategory;}
    public List<CategoryBreakdown> getExpenseByCategory(){return expenseByCategory;}
    public void setTotalIncome(BigDecimal t){this.totalIncome=t;}
    public void setTotalExpense(BigDecimal t){this.totalExpense=t;}
    public void setSavings(BigDecimal s){this.savings=s;}
    public void setIncomeByCategory(List<CategoryBreakdown> l){this.incomeByCategory=l;}
    public void setExpenseByCategory(List<CategoryBreakdown> l){this.expenseByCategory=l;}
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private BigDecimal totalIncome,totalExpense,savings;
        private List<CategoryBreakdown> incomeByCategory,expenseByCategory;
        public Builder totalIncome(BigDecimal t){this.totalIncome=t;return this;}
        public Builder totalExpense(BigDecimal t){this.totalExpense=t;return this;}
        public Builder savings(BigDecimal s){this.savings=s;return this;}
        public Builder incomeByCategory(List<CategoryBreakdown> l){this.incomeByCategory=l;return this;}
        public Builder expenseByCategory(List<CategoryBreakdown> l){this.expenseByCategory=l;return this;}
        public DashboardResponse build(){
            DashboardResponse r=new DashboardResponse();
            r.totalIncome=totalIncome;r.totalExpense=totalExpense;r.savings=savings;
            r.incomeByCategory=incomeByCategory;r.expenseByCategory=expenseByCategory;return r;
        }
    }
    public static class CategoryBreakdown {
        private String category; private BigDecimal amount; private Long count;
        public CategoryBreakdown(String category, BigDecimal amount, Long count){
            this.category=category;this.amount=amount;this.count=count;
        }
        public String getCategory(){return category;}
        public BigDecimal getAmount(){return amount;}
        public Long getCount(){return count;}
    }
}