package com.example.credit_card_register.cardregister.repository;

import com.example.credit_card_register.cardregister.entity.CreditCardData;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface CreditCardDataRepository extends JpaRepository<CreditCardData, Long> {
   Optional<CreditCardData> findByUserDataOib(Long oib);

}
