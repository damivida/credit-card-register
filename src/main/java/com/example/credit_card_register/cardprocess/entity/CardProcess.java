package com.example.credit_card_register.cardprocess.entity;


import com.example.credit_card_register.cardregister.entity.CreditCardStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "card_process")
public class CardProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "users_oib")
    private String oib;

    @Column(name = "credit_card_status")
    @Enumerated(EnumType.STRING)
    private CreditCardStatus creditCardStatus;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
