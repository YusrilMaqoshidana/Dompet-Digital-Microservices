package com.microservice.topupservice.repository;

import java.util.Optional;

import com.microservice.topupservice.models.TransactionStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.topupservice.models.TopupModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopupRepository extends JpaRepository<TopupModel, String>{
    Optional<TopupModel> findByTopupId(String topupId);
    Optional<TopupModel> findByExternalTransactionId(String externalTransactionId);


    @Modifying
    @Transactional
    @Query("UPDATE TopupModel t SET t.status = :status WHERE t.externalTransactionId = :externalId")
    void updateStatusByExternalId(@Param("externalId") String externalId, @Param("status") TransactionStatus status);

}
