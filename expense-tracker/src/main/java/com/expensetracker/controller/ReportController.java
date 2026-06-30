package com.expensetracker.controller;
import com.expensetracker.dto.MonthlyReportResponse;
import com.expensetracker.service.ReportService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) { this.reportService=reportService; }
    @GetMapping("/monthly")
    public MonthlyReportResponse getMonthly(@RequestParam(defaultValue="0") int year) {
        if (year == 0) year = LocalDate.now().getYear();
        return reportService.getMonthlyReport(year);
    }
    @GetMapping("/years")
    public List<Integer> getAvailableYears() { return reportService.getAvailableYears(); }
}