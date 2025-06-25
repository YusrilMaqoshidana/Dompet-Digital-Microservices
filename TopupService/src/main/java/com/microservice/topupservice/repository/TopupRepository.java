package com.microservice.topupservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.topupservice.models.TopupModel;

public interface TopupRepository extends JpaRepository<TopupModel, String>{
    Optional<TopupModel> findByTopupId(String topupId);
    Optional<TopupModel> findByExternalTransactionId(String externalTransactionId);
}
