package com.example.credit_card_register.cardregister.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "credit_card_data")
public class CreditCardData {

    @Id
    private Long id;

    @Column(name = "credit_card_status")
    @Enumerated(EnumType.STRING)
    private CreditCardStatus creditCardStatus;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserData userData;

}
