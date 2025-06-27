package com.microservice.notificationservice.services;

import com.microservice.notificationservice.DTO.TopupModelResponse;
import com.microservice.notificationservice.DTO.TransactionModelResponse;
import com.microservice.notificationservice.models.NotificationModel;
import com.microservice.notificationservice.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void createNotificationFromTopup(TopupModelResponse event) {
        NotificationModel notificationModel = eventToTopupResponse(event);
        notificationRepository.save(notificationModel);
    }

    public void createNotificationFromSenderTransaction(TransactionModelResponse event) {
        NotificationModel notificationModel = eventToSenderTransactionResponse(event);
        notificationRepository.save(notificationModel);
    }

    public void createNotificationFromReceiverTransaction(TransactionModelResponse event) {
        NotificationModel notificationModel = eventToReceiverTransactionResponse(event);
        notificationRepository.save(notificationModel);
    }

    public List<NotificationModel> getUserNotification(String userId) {
        return notificationRepository.findNotificationModelByUserId(userId);
    }

   private NotificationModel eventToTopupResponse(TopupModelResponse event) {
    String generateId = UUID.randomUUID().toString();
    String now = LocalDateTime.now().toString();
    String message;
    if ("CREDIT".equalsIgnoreCase(event.getType())) {
        message = "Top-up sebesar " + event.getAmount() + " telah " + event.getStatus().toLowerCase() + ". Saldo Anda telah bertambah.";
    } else if ("DEBIT".equalsIgnoreCase(event.getType())) {
        message = "Transaksi debit sebesar " + event.getAmount() + " telah " + event.getStatus().toLowerCase() + ". Saldo Anda telah berkurang.";
    } else {
        message = "Transaksi sebesar " + event.getAmount() + " statusnya " + event.getStatus().toLowerCase();
    }
    return NotificationModel.builder()
            .notificationId(generateId)
            .userId(event.getUserId())
            .type(NotificationModel.NotificationType.TOPUP)
            .message(message)
            .createdAt(now)
            .build();
}

    private NotificationModel eventToSenderTransactionResponse(TransactionModelResponse event) {
        String generateId = UUID.randomUUID().toString();
        String now = LocalDateTime.now().toString();
        return NotificationModel.builder()
                .notificationId(generateId)
                .userId(event.getSenderUserId())
                .type(NotificationModel.NotificationType.TRANSACTION)
                .message("Pengiriman dana ke ID " + event.getReceiverUserId() + " dengan Jumlah " + event.getAmount() + " " + event.getStatus())
                .createdAt(now)
                .build();
    }

    private NotificationModel eventToReceiverTransactionResponse(TransactionModelResponse event) {
        String generateId = UUID.randomUUID().toString();
        String now = LocalDateTime.now().toString();
        return NotificationModel.builder()
                .notificationId(generateId)
                .userId(event.getReceiverUserId())
                .type(NotificationModel.NotificationType.TRANSACTION)
                .message("Kamu menerima dana dari ID " + event.getSenderUserId() + " dengan Jumlah " + event.getAmount())
                .createdAt(now)
                .build();
    }
}
