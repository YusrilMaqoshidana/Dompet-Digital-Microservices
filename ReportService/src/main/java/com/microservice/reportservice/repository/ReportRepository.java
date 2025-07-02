package com.microservice.reportservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.reportservice.models.ReportModel;

public interface ReportRepository extends JpaRepository<ReportModel, String> {
    Page<ReportModel> findByUserId(String userId, Pageable pageable);
    Optional<ReportModel> findByReportId(String reportId);
}
