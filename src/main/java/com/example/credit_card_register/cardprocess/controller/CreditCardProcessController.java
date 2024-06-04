package com.example.credit_card_register.cardprocess.controller;

import com.example.credit_card_register.cardprocess.service.CreditCardProcessService;
import com.example.credit_card_register.core.dto.CardRequest;
import com.example.credit_card_register.core.dto.BaseDto;
import com.example.credit_card_register.core.openapi.OpenApiTags;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/card-request")
@RequiredArgsConstructor
public class CreditCardProcessController {

    private final CreditCardProcessService creditCardProcessService;

    @PostMapping("")
    @Operation(tags = OpenApiTags.USER_CARD_PROCESS, summary = "Credit card production request.")
    public ResponseEntity<BaseDto> createCreditCardRequest(@RequestBody CardRequest requestData) {
        BaseDto baseDto = creditCardProcessService.createCreditCardRequest(requestData);
        return new ResponseEntity<>(baseDto, baseDto.getStatus());
    }
}
