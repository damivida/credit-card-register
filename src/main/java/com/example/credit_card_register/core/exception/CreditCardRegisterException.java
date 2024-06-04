package com.example.credit_card_register.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;


@Data
@EqualsAndHashCode(callSuper = true)
public class CreditCardRegisterException extends Exception{

    private final HttpStatus status;
    private final String code;
    private final String id;
    private final String description;


    public CreditCardRegisterException(HttpStatus status, String code, String id, String description) {
        this.status = status;
        this.code = code;
        this.id = id;
        this.description = description;
    }

    public CreditCardRegisterException(String message, HttpStatus status, String code, String id, String description) {
        super(message);
        this.status = status;
        this.code = code;
        this.id = id;
        this.description = description;
    }

}
