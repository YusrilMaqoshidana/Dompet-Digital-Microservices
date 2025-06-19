package com.microservice.userservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserModel {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_registered", nullable = false)
    private LocalDateTime dateRegistered;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

}