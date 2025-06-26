package com.microservice.notificationservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class NotificationModel {
    @Id
    @Column(name = "notification_id")
    private String notificationId;
    @Column(name = "user_id")
    private String userId; // ID pengguna yang menerima notifikasi
    @Column(name = "type")
    private NotificationType type; // Jenis notifikasi (mis. TOPUP, TRANSACTION)
    @Column(name = "message")
    private String message;
    @Column(name = "created_at")
    private String createdAt;

    public enum NotificationType {
        TOPUP,
        TRANSACTION,
        GENERAL
    }
}
