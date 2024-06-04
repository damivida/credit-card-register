package com.example.credit_card_register.cardregister.controller;


import com.example.credit_card_register.cardregister.dto.UserCardRegisterRequest;
import com.example.credit_card_register.cardregister.dto.UserCardRegisterResponse;
import com.example.credit_card_register.cardregister.service.UserCreditCardService;
import com.example.credit_card_register.core.dto.BaseDto;
import com.example.credit_card_register.core.exception.CreditCardRegisterException;
import com.example.credit_card_register.core.openapi.OpenApiTags;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-card-register")
public class UserCreditCardController {


    private final UserCreditCardService userCreditCardService;

    @PostMapping("/")
    @Operation(tags = OpenApiTags.USER_CARD_REGISTER, summary = "Register user for credit card request.")
    public ResponseEntity<UserCardRegisterResponse> registerUserCreditCard(@Valid @RequestBody UserCardRegisterRequest request) throws Exception {
        return new ResponseEntity<>(userCreditCardService.registerUserCreditCard(request), HttpStatus.OK);
    }

    @GetMapping("/{oib}")
    @Operation(tags = OpenApiTags.USER_CARD_REGISTER, summary = "Get users credit card data.")
    public ResponseEntity<UserCardRegisterResponse> getCreditCardDataByOib(@PathVariable Long oib) throws Exception {
        return new ResponseEntity<>(userCreditCardService.getCreditCardDataByOib(oib), HttpStatus.OK);
    }

    @GetMapping("/send-user-data/{oib}")
    @Operation(tags = OpenApiTags.USER_CARD_REGISTER, summary = "Create credit card request.")
    public ResponseEntity<BaseDto> cardRequest(@PathVariable Long oib) throws CreditCardRegisterException {
        return new ResponseEntity<>(userCreditCardService.cardRequest(oib), HttpStatus.OK);
    }

    @DeleteMapping("/{oib}")
    @Operation(tags = OpenApiTags.USER_CARD_REGISTER, summary = "Delete user.")
    public ResponseEntity<BaseDto> deleteUser(@NonNull @PathVariable Long oib) throws CreditCardRegisterException {
        BaseDto baseDto = userCreditCardService.deleteUser(oib);
        return new ResponseEntity<>(baseDto, baseDto.getStatus());
    }
}
