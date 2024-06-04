package com.example.credit_card_register.cardregister.dto;
import com.example.credit_card_register.cardregister.entity.CreditCardStatus;
import lombok.Data;


@Data
public class UserCardRegisterResponse {
    private Long oib;
    private String firstName;
    private String lastName;
    private CreditCardStatus creditCardStatus;
    private String description;
}
