package com.microservice.transactionservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class TransactionModel {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    // Pengirim
    @Column(name= "sender_user_id")
    private String senderUserId;

    // Penerima
    @Column(name= "receiver_user_id")
    private String receiverUserId;

    // Jumlah Transfer
    @Column(name= "amount")
    private float amount;

    // Selalu USER_TRANSFER
    @Column(name= "transaction_type")
    private String transactionType;

    // Ketika di inisiasi
    @Column(name = "created_at")
    private String createdAt;

    // Ketika proses selesai (bisa gagal atau sukses)
    @Column(name = "completed_at")
    private String completedAt;

    // Deskripsi
    @Column(name = "description")
    private String description;

    // Status ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED')
    @Column(name = "status")
    private String status;
}
