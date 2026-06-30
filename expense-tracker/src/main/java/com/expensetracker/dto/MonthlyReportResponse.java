package com.expensetracker.dto;
import java.math.BigDecimal;
import java.util.List;
public class MonthlyReportResponse {
    private int year;
    private List<MonthData> months;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal totalSavings;
    public MonthlyReportResponse() {}
    public int getYear(){return year;}
    public List<MonthData> getMonths(){return months;}
    public BigDecimal getTotalIncome(){return totalIncome;}
    public BigDecimal getTotalExpense(){return totalExpense;}
    public BigDecimal getTotalSavings(){return totalSavings;}
    public void setYear(int y){this.year=y;}
    public void setMonths(List<MonthData> m){this.months=m;}
    public void setTotalIncome(BigDecimal t){this.totalIncome=t;}
    public void setTotalExpense(BigDecimal t){this.totalExpense=t;}
    public void setTotalSavings(BigDecimal t){this.totalSavings=t;}
    public static class MonthData {
        private int month; private String monthName;
        private BigDecimal income; private BigDecimal expense; private BigDecimal savings;
        public MonthData(int month, String monthName, BigDecimal income, BigDecimal expense) {
            this.month=month; this.monthName=monthName;
            this.income=income; this.expense=expense;
            this.savings=income.subtract(expense);
        }
        public int getMonth(){return month;}
        public String getMonthName(){return monthName;}
        public BigDecimal getIncome(){return income;}
        public BigDecimal getExpense(){return expense;}
        public BigDecimal getSavings(){return savings;}
    }
}