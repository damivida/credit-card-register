package com.example.credit_card_register.cardprocess.service;

import com.example.credit_card_register.cardprocess.entity.CardProcess;
import com.example.credit_card_register.cardprocess.kafkaproducer.MessageProducer;
import com.example.credit_card_register.cardprocess.repository.CardProcessRepository;
import com.example.credit_card_register.cardregister.entity.CreditCardStatus;
import com.example.credit_card_register.core.dto.BaseDto;
import com.example.credit_card_register.core.dto.CardRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditCardProcessService {

    private final MessageProducer messageProducer;
    private final CardProcessRepository cardProcessRepository;


    public BaseDto createCreditCardRequest(CardRequest requestData) {
        CardProcess cardProcess = new CardProcess();
        cardProcess.setOib(requestData.getOib());
        cardProcess.setCreditCardStatus(CreditCardStatus.IN_PROGRESS);
        cardProcessRepository.save(cardProcess);
        log.info("Received card data: {}", requestData);
        return new BaseDto(HttpStatus.OK, "200", "1", "New card request successfully created.");
    }

    @Scheduled(fixedRate = 30000)
    public void createCreditCard() {

      Optional<CardProcess> cardCreated = cardProcessRepository.findOldestCardPrecessRecord();

        if(cardCreated.isPresent()) {
            messageProducer.sendMessage("card-process-topic", cardCreated.get().getOib());

            CardProcess cardProcess = cardCreated.get();
            cardProcess.setCreditCardStatus(CreditCardStatus.DONE);
            cardProcessRepository.save(cardProcess);
            log.info("Event from producer, card created for OIB: {}", cardCreated.get().getOib());
        }else {
            log.info("Action from producer, no data to precess: {}", new Date());
        }

    }
}
