package com.example.parisjanitormsuser.dto;

import com.example.parisjanitormsuser.entity.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class PassResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime expiryDate;

    public PassResetToken() {}

    public PassResetToken(String token, User user, int expirationMinutes) {
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(expirationMinutes);
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    // Getters & Setters
}
