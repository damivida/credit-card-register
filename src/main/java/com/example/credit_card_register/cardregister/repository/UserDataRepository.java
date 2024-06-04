package com.example.credit_card_register.cardregister.repository;

import com.example.credit_card_register.cardregister.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByOib(Long oib);
}
