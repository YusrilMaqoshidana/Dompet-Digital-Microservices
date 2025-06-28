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
        String message;
        String status = event.getStatus().toLowerCase();
        String type = event.getType().toUpperCase();

        if ("SUCCESS".equalsIgnoreCase(status)) {
            if ("CREDIT".equals(type)) {
                message = "Top-up sebesar " + event.getAmount() + " telah berhasil. Saldo Anda telah bertambah.";
            } else if ("DEBIT".equals(type)) {
                message = "Transaksi debit sebesar " + event.getAmount() + " telah berhasil. Saldo Anda telah berkurang.";
            } else {
                message = "Transaksi sebesar " + event.getAmount() + " telah berhasil.";
            }
        } else if ("FAILED".equalsIgnoreCase(status)) {
            if ("CREDIT".equals(type)) {
                message = "Top-up sebesar " + event.getAmount() + " gagal. Saldo Anda tidak berubah. Silakan coba lagi.";
            } else if ("DEBIT".equals(type)) {
                message = "Transaksi debit sebesar " + event.getAmount() + " gagal. Saldo Anda tidak berkurang.";
            } else {
                message = "Transaksi sebesar " + event.getAmount() + " gagal.";
            }
        } else {
            message = "Transaksi sebesar " + event.getAmount() + " statusnya " + status + ".";
        }

        return NotificationModel.builder()
                .notificationId(generateId)
                .userId(event.getUserId())
                .type(NotificationModel.NotificationType.TOPUP)
                .message(message)
                .build();
    }

    private NotificationModel eventToSenderTransactionResponse(TransactionModelResponse event) {
        String generateId = UUID.randomUUID().toString();
        return NotificationModel.builder()
                .notificationId(generateId)
                .userId(event.getSenderUserId())
                .type(NotificationModel.NotificationType.TRANSACTION)
                .message("Pengiriman dana ke ID " + event.getReceiverUserId() + " dengan Jumlah " + event.getAmount() + " " + event.getStatus())
                .build();
    }

    private NotificationModel eventToReceiverTransactionResponse(TransactionModelResponse event) {
        String generateId = UUID.randomUUID().toString();
        return NotificationModel.builder()
                .notificationId(generateId)
                .userId(event.getReceiverUserId())
                .type(NotificationModel.NotificationType.TRANSACTION)
                .message("Kamu menerima dana dari ID " + event.getSenderUserId() + " dengan Jumlah " + event.getAmount())
                .build();
    }
}
