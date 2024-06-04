package com.example.credit_card_register.cardprocess.repository;

import com.example.credit_card_register.cardprocess.entity.CardProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardProcessRepository extends JpaRepository<CardProcess, Long> {

    @Query(value = "SELECT * FROM card_process "
            + "WHERE credit_card_status = 'IN_PROGRESS' "
            + "ORDER BY created_at ASC "
            + "LIMIT 1", nativeQuery = true)
   Optional <CardProcess> findOldestCardPrecessRecord();

}
