package com.example.credit_card_register.cardregister.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users_data")
public class UserData {

    @Id
    private Long oib;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "created_at",  nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "userData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CreditCardData creditCardData;

}
