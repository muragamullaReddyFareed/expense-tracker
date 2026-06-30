package com.expensetracker.service;
import com.expensetracker.dto.MonthlyReportResponse;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.security.AuthenticatedUserProvider;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Month;
import java.util.*;
@Service
public class ReportService {
    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserProvider userProvider;
    public ReportService(TransactionRepository tr, AuthenticatedUserProvider up) {
        this.transactionRepository=tr; this.userProvider=up;
    }
    public MonthlyReportResponse getMonthlyReport(int year) {
        Long uid = userProvider.getCurrentUser().getId();
        List<Object[]> incomeData  = transactionRepository.getMonthlyReport(uid, TransactionType.INCOME,  year);
        List<Object[]> expenseData = transactionRepository.getMonthlyReport(uid, TransactionType.EXPENSE, year);
        Map<Integer, BigDecimal> incomeMap  = new HashMap<>();
        Map<Integer, BigDecimal> expenseMap = new HashMap<>();
        for (Object[] r : incomeData)  incomeMap.put(((Number)r[1]).intValue(),  (BigDecimal)r[2]);
        for (Object[] r : expenseData) expenseMap.put(((Number)r[1]).intValue(), (BigDecimal)r[2]);
        List<MonthlyReportResponse.MonthData> months = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO, totalExpense = BigDecimal.ZERO;
        for (int m = 1; m <= 12; m++) {
            BigDecimal inc = incomeMap.getOrDefault(m,  BigDecimal.ZERO);
            BigDecimal exp = expenseMap.getOrDefault(m, BigDecimal.ZERO);
            months.add(new MonthlyReportResponse.MonthData(m, Month.of(m).name(), inc, exp));
            totalIncome  = totalIncome.add(inc);
            totalExpense = totalExpense.add(exp);
        }
        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setYear(year); response.setMonths(months);
        response.setTotalIncome(totalIncome); response.setTotalExpense(totalExpense);
        response.setTotalSavings(totalIncome.subtract(totalExpense));
        return response;
    }
    public List<Integer> getAvailableYears() {
        Long uid = userProvider.getCurrentUser().getId();
        List<Integer> years = transactionRepository.getAvailableYears(uid);
        if (years.isEmpty()) years = List.of(java.time.LocalDate.now().getYear());
        return years;
    }
}