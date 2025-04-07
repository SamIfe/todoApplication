package net.todoApplication.controllers;

import lombok.RequiredArgsConstructor;
import net.todoApplication.services.interfaces.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {


    private final ReportService reportService;

    @GetMapping("/user-activity")
    public ResponseEntity<Map<String, Object>> getUserActivityReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        Map<String, Object> report = reportService.generateUserActivityReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/completion-rate")
    public ResponseEntity<Map<String, Object>> getCompletionRateReport(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        Map<String, Object> report = reportService.generateCompletionRateReport(userId, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/category-distribution")
    public ResponseEntity<Map<String, Object>> getCategoryDistributionReport(@RequestParam String userId) {
        Map<String, Object> report = reportService.generateCategoryDistributionReport(userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/my-completion-rate")
    public ResponseEntity<Map<String, Object>> getMyCompletionRateReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = getUserIdFromEmail(authentication.getName());

        Map<String, Object> report = reportService.generateCompletionRateReport(userId, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/my-category-distribution")
    public ResponseEntity<Map<String, Object>> getMyCategoryDistributionReport() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = getUserIdFromEmail(authentication.getName());

        Map<String, Object> report = reportService.generateCategoryDistributionReport(userId);
        return ResponseEntity.ok(report);
    }

    private String getUserIdFromEmail(String email) {
        return "user-id";
    }
}

