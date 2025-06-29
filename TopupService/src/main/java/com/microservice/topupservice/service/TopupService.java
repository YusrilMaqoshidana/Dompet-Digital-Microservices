package com.microservice.topupservice.service;

import com.microservice.topupservice.DTO.TopupDTO;
import com.microservice.topupservice.DTO.TopupRequestDTO;
import com.microservice.topupservice.DTO.WalletEventDTO;
import com.microservice.topupservice.kafka.TopupPublisherService;
import com.microservice.topupservice.models.TopupModel;
import com.microservice.topupservice.models.TransactionStatus;
import com.microservice.topupservice.repository.TopupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopupService {
    private final TopupRepository topupRepository;
    private final TopupPublisherService topupPublisherService;

    public List<TopupModel> getAll() {
        return topupRepository.findAll();
    }

    public TopupModel getByTopupId(String topupId){
        return topupRepository.findByTopupId(topupId)
                .orElse(null);
    }

    @Transactional
    public TopupModel create(TopupRequestDTO requestDTO) {
        log.info("Received a request to create a new topup transaction with external ID: {}", requestDTO.getExternalTransactionId());
        topupRepository.findByExternalTransactionId(requestDTO.getExternalTransactionId())
                .ifPresent(tx -> {
                    log.warn("Duplicate transaction attempt with external ID: {}", requestDTO.getExternalTransactionId());
                    throw new IllegalStateException("Transaction with ID " + requestDTO.getExternalTransactionId() + " already exists.");
                });
        TopupModel topup = toTopupModel(requestDTO);
        TopupModel savedTopup = topupRepository.save(topup);
        log.info("Transaction with external ID {} has been saved to DB with PENDING status. Internal ID: {}",
                savedTopup.getExternalTransactionId(), savedTopup.getTopupId());

        TopupDTO kafkaDto = TopupDTO.builder()
                .externalTransactionId(savedTopup.getExternalTransactionId())
                .userId(savedTopup.getUserId())
                .amount(savedTopup.getAmount())
                .type(savedTopup.getType())
                .build();
        topupPublisherService.publishTopupInitiatedEvent(kafkaDto);

        return savedTopup;
    }

    @Transactional
    public void finalizeTopup(WalletEventDTO event) {
        log.info("Finalizing transaction for external ID: {}", event.getExternalTransactionId());

        TopupModel originalTopup = topupRepository.findByExternalTransactionId(event.getExternalTransactionId())
                .orElseThrow(() -> {
                    log.error("Cannot finalize transaction. Transaction not found for external ID: {}", event.getExternalTransactionId());
                    return new EntityNotFoundException("Topup transaction not found with external ID: " + event.getExternalTransactionId());
                });
        if (originalTopup.getStatus() != TransactionStatus.PENDING) {
            log.warn("Ignoring event for an already finalized transaction. External ID: {}, Current Status: {}.",
                    originalTopup.getExternalTransactionId(), originalTopup.getStatus());
            return;
        }
        boolean isSuccess = event.isSuccess();
        TransactionStatus newStatus = isSuccess ? TransactionStatus.SUCCESS : TransactionStatus.FAILED;

        topupRepository.updateStatusByExternalId(event.getExternalTransactionId(), newStatus);
        log.info("Transaction {} successfully updated to {}.", originalTopup.getExternalTransactionId(), newStatus);
        TopupModel updatedTopup = new TopupModel();
        updatedTopup.setTopupId(originalTopup.getTopupId());
        updatedTopup.setExternalTransactionId(originalTopup.getExternalTransactionId());
        updatedTopup.setUserId(originalTopup.getUserId());
        updatedTopup.setAmount(originalTopup.getAmount());
        updatedTopup.setType(originalTopup.getType());
        updatedTopup.setCreatedAt(originalTopup.getCreatedAt());
        updatedTopup.setStatus(newStatus);
        if (isSuccess) {
            topupPublisherService.publishTopupSuccessEvent(updatedTopup);
        } else {
            topupPublisherService.publishTopupFailedEvent(updatedTopup);
        }
    }


    private TopupModel toTopupModel(TopupRequestDTO dto) {
        TopupModel topup = new TopupModel();
        topup.setTopupId(UUID.randomUUID().toString());
        topup.setExternalTransactionId(dto.getExternalTransactionId());
        topup.setUserId(dto.getUserId());
        topup.setAmount(dto.getAmount());
        topup.setType(dto.getType());
        topup.setStatus(TransactionStatus.PENDING);
        return topup;
    }
}