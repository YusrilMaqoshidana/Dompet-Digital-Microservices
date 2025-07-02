package com.microservice.reportservice.controllers;

import com.microservice.reportservice.DTO.ApiResponse;
import com.microservice.reportservice.models.ReportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.reportservice.DTO.ReportResponse;
import com.microservice.reportservice.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<ReportModel>> getUserReport(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ReportModel> userReport = reportService.getUserReport(userId, pageable);
        return ResponseEntity.ok(userReport);
    }

    @DeleteMapping("/{report_id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserReport(@PathVariable("report_id") String reportId) {
        reportService.deleteReportById(reportId);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Report deleted successfully"
        );
        return ResponseEntity.ok(response);
    }


}
