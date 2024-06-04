package com.example.credit_card_register.cardregister.service;

import com.example.credit_card_register.cardregister.dto.UserCardRegisterRequest;
import com.example.credit_card_register.cardregister.dto.UserCardRegisterResponse;
import com.example.credit_card_register.cardregister.entity.CreditCardData;
import com.example.credit_card_register.cardregister.entity.CreditCardStatus;
import com.example.credit_card_register.cardregister.entity.UserData;
import com.example.credit_card_register.cardregister.repository.CreditCardDataRepository;
import com.example.credit_card_register.cardregister.repository.UserDataRepository;
import com.example.credit_card_register.core.dto.CardRequest;
import com.example.credit_card_register.core.dto.BaseDto;
import com.example.credit_card_register.core.exception.CreditCardRegisterException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Service
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCreditCardService {

    private final UserDataRepository userDataRepository;
    private final CreditCardDataRepository creditCardDataRepository;
    private final RestTemplate restTemplate;


    @Transactional
    public UserCardRegisterResponse registerUserCreditCard(UserCardRegisterRequest request) throws CreditCardRegisterException {


        if(request.getOib().toString().length() != 11) {
            throw new CreditCardRegisterException(HttpStatus.BAD_REQUEST, "400", "4011", "Oib must have 11 digits.");

        }
        Optional<UserData> user = userDataRepository.findByOib(request.getOib());

        if (user.isPresent()) {
            throw new CreditCardRegisterException(HttpStatus.CONFLICT, "409", "4009", "User already exist.");
        }

        UserData userData = userDataRepository.save(toEntitUserData(request));
        CreditCardData creditCardData = creditCardDataRepository.save(toEntityCreditCard(request, userData));
        return fromEntityCreditCardData(creditCardData);

    }


    public UserCardRegisterResponse getCreditCardDataByOib(Long oib) throws CreditCardRegisterException {
        Optional<CreditCardData> creditCardData = creditCardDataRepository.findByUserDataOib(oib);

        if (creditCardData.isEmpty()) {
            throw new CreditCardRegisterException(HttpStatus.NOT_FOUND, "404", "4004", "User not found.");
        }

        return fromEntityCreditCardData(creditCardData.get());
    }


    public BaseDto cardRequest(Long oib) throws CreditCardRegisterException {

        CreditCardData creditCardData = getCreditCardData(oib);

        if(creditCardData.getCreditCardStatus() == CreditCardStatus.DONE  ||
                creditCardData.getCreditCardStatus() == CreditCardStatus.IN_PROGRESS) {
            throw new CreditCardRegisterException(HttpStatus.BAD_REQUEST, "400", "4007", "Card request already precessed.");
        }

        // send data to card process API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CardRequest cardRequest = toCardRequest(creditCardData);
        HttpEntity<CardRequest> request = new HttpEntity<>(cardRequest, headers);

        BaseDto baseDto;

        try {
            baseDto = restTemplate.postForObject("http://localhost:8080/api/v1/card-request", request, BaseDto.class);
            creditCardData.setDescription("Sent to card process.");
            creditCardData.setCreditCardStatus(CreditCardStatus.IN_PROGRESS);
            creditCardDataRepository.save(creditCardData);
        } catch (Exception e) {
            baseDto = new BaseDto(HttpStatus.SERVICE_UNAVAILABLE, "503", "Unable to send card data to card process.");
        }

        return baseDto;
    }


    public BaseDto deleteUser(Long oib) throws CreditCardRegisterException {
        Optional<UserData> userData = userDataRepository.findByOib(oib);

        if (userData.isEmpty()) {
            throw new CreditCardRegisterException(HttpStatus.NOT_FOUND, "404", "4004", "User not found.");
        }

        userDataRepository.delete(userData.get());
        return new BaseDto(HttpStatus.OK, "200", String.format("Deleted user with OIB: %s", oib));

    }

    @KafkaListener(topics = "card-process-topic", groupId = "my-group-id")
    public void updateCreditCardData(String message) throws CreditCardRegisterException {

        CreditCardData creditCardData = getCreditCardData(Long.valueOf(message));
        creditCardData.setCreditCardStatus(CreditCardStatus.DONE);
        creditCardDataRepository.save(creditCardData);
        log.info("Event from consumer, card created for OIB: {}", message);
    }

    public CreditCardData getCreditCardData(Long oib) throws CreditCardRegisterException {
        Optional<CreditCardData> creditCardData = creditCardDataRepository.findByUserDataOib(oib);

        if (creditCardData.isEmpty()) {
            throw new CreditCardRegisterException(HttpStatus.NOT_FOUND, "404", "4004", "User not found.");
        }
        return creditCardData.get();
    }


    //-- mapper methods
    public UserData toEntitUserData(UserCardRegisterRequest request) {
        UserData userData = new UserData();
        userData.setOib(Long.valueOf(request.getOib()));
        userData.setFirstName(request.getFirstName());
        userData.setLastName(request.getLastName());
        return userData;
    }

    public CreditCardData toEntityCreditCard(UserCardRegisterRequest request, UserData userData) {
        CreditCardData creditCardData = new CreditCardData();
        creditCardData.setCreditCardStatus(CreditCardStatus.REQUESTED);
        creditCardData.setUserData(userData);
        creditCardData.setDescription(request.getDescription());
        return creditCardData;
    }

    public UserCardRegisterResponse fromEntityCreditCardData(CreditCardData creditCardData) {
        UserCardRegisterResponse userCardRegisterResponse = new UserCardRegisterResponse();
        userCardRegisterResponse.setOib(creditCardData.getUserData().getOib());
        userCardRegisterResponse.setFirstName(creditCardData.getUserData().getFirstName());
        userCardRegisterResponse.setLastName(creditCardData.getUserData().getLastName());
        userCardRegisterResponse.setCreditCardStatus(creditCardData.getCreditCardStatus());
        userCardRegisterResponse.setDescription(creditCardData.getDescription());
        return userCardRegisterResponse;
    }

    public CardRequest toCardRequest(CreditCardData creditCardData) {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setOib(String.valueOf(creditCardData.getUserData().getOib()));
        cardRequest.setLastName(creditCardData.getUserData().getLastName());
        cardRequest.setFirstName(creditCardData.getUserData().getFirstName());
        cardRequest.setStatus(String.valueOf(creditCardData.getCreditCardStatus()));
        return cardRequest;
    }

}
