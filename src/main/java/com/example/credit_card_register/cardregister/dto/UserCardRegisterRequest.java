package com.example.credit_card_register.cardregister.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserCardRegisterRequest {

    @NotNull(message = "Oib must not be null")
    private Long oib;

    @NotBlank(message = "FirstName cannot be blank")
    @Size(message = "Size must be from 1 to 255", min = 1, max = 255)
    private String firstName;

    @NotBlank(message = "lastName cannot be blank")
    @Size(message = "Size must be from 1 to 255", min = 1, max = 255)
    private String lastName;

    private String description;
}
