package com.example.credit_card_register.core.dto;

import lombok.Data;

@Data
public class CardRequest {

    private String firstName;
    private String lastName;
    private String status;
    private String oib;

}
