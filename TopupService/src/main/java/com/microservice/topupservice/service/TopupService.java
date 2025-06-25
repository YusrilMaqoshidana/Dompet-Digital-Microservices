package com.microservice.topupservice.service;

import com.microservice.topupservice.DTO.TopupDTO;
import com.microservice.topupservice.DTO.TopupRequestDTO;
import com.microservice.topupservice.DTO.WalletEventDTO;
import com.microservice.topupservice.kafka.TopupPublisherService;
import com.microservice.topupservice.models.TopupModel;
import com.microservice.topupservice.models.TransactionStatus;
import com.microservice.topupservice.repository.TopupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        topupRepository.findByExternalTransactionId(requestDTO.getExternalTransactionId())
            .ifPresent(tx -> {
                log.warn("Duplicate transaction attempt with external ID: {}", requestDTO.getExternalTransactionId());
                throw new IllegalStateException("Transaction with ID " + requestDTO.getExternalTransactionId() + " already exists.");
            });

        TopupModel topup = toTopupModel(requestDTO);
        TopupModel savedTopup = topupRepository.save(topup);
        log.info("Transaction saved to DB with PENDING status. Internal ID: {}", savedTopup.getTopupId());

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
        topupRepository.findByExternalTransactionId(event.getExternalTransactionId()).ifPresentOrElse(
            topup -> {
                if (topup.getStatus() == TransactionStatus.PENDING) {
                    topup.setStatus(event.isSuccess() ? TransactionStatus.SUCCESS : TransactionStatus.FAILED);
                    topupRepository.save(topup);
                    log.info("Transaction {} updated to {}.", topup.getExternalTransactionId(), topup.getStatus());
                } else {
                    log.warn("Received event for already finalized transaction ID: {}", topup.getExternalTransactionId());
                }
            },
            () -> log.error("Received wallet event but transaction not found for external ID: {}", event.getExternalTransactionId())
        );
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