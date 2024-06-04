package com.example.credit_card_register.cardregister;

import com.example.credit_card_register.cardregister.dto.UserCardRegisterRequest;
import com.example.credit_card_register.cardregister.dto.UserCardRegisterResponse;
import com.example.credit_card_register.cardregister.entity.CreditCardData;
import com.example.credit_card_register.cardregister.entity.UserData;
import com.example.credit_card_register.cardregister.repository.CreditCardDataRepository;
import com.example.credit_card_register.cardregister.repository.UserDataRepository;
import com.example.credit_card_register.cardregister.service.UserCreditCardService;
import com.example.credit_card_register.core.exception.CreditCardRegisterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserCreditCardRegisterServiceTest {


    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private CreditCardDataRepository creditCardDataRepository;

    @InjectMocks
    private UserCreditCardService userCreditCardService;

    private UserCardRegisterRequest userCardRegisterRequest;
    private UserData userData;
    private CreditCardData creditCardData;



    @BeforeEach
    void setUp() {
        userCardRegisterRequest = new UserCardRegisterRequest();
        userCardRegisterRequest.setOib(12345678910L);
        userCardRegisterRequest.setFirstName("first");
        userCardRegisterRequest.setLastName("last");

        userData = new UserData();
        userData.setOib(123456789L);
        userData.setLastName("last");
        userData.setFirstName("first");

        creditCardData = new CreditCardData();
        creditCardData.setUserData(userData);

    }

    @Test
    void testRegisterUserCreditCard_NewUser() throws CreditCardRegisterException {
        when(userDataRepository.findByOib(anyLong())).thenReturn(Optional.empty());
        when(userDataRepository.save(Mockito.any(UserData.class))).thenReturn(userData);
        when(creditCardDataRepository.save(Mockito.any(CreditCardData.class))).thenReturn(creditCardData);

        UserCardRegisterResponse result = userCreditCardService.registerUserCreditCard(userCardRegisterRequest);

        assertNotNull(result);
        assertEquals("last", result.getLastName());
        verify(userDataRepository).save(Mockito.any(UserData.class));
        verify(creditCardDataRepository).save(Mockito.any(CreditCardData.class));
    }


    @Test
    void testRegisterUserCreditCard_UserAlreadyExists() {
        when(userDataRepository.findByOib(anyLong())).thenReturn(Optional.of(userData));

        CreditCardRegisterException exception = assertThrows(CreditCardRegisterException.class, () -> {
            userCreditCardService.registerUserCreditCard(userCardRegisterRequest);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("409", exception.getCode());
        assertEquals("4009", exception.getId());
        assertEquals("User already exist.", exception.getDescription());
        verify(userDataRepository, never()).save(Mockito.any(UserData.class));
        verify(creditCardDataRepository, never()).save(Mockito.any(CreditCardData.class));
    }


    @Test
    void testRegisterUserCreditCard_InvalidOibLength() {
        userCardRegisterRequest.setOib(1234567L);

        CreditCardRegisterException exception = assertThrows(CreditCardRegisterException.class, () -> {
            userCreditCardService.registerUserCreditCard(userCardRegisterRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("400", exception.getCode());
        assertEquals("4011", exception.getId());
        assertEquals("Oib must have 11 digits.", exception.getDescription());
        verify(userDataRepository, never()).findByOib(anyLong());
        verify(userDataRepository, never()).save(any(UserData.class));
        verify(creditCardDataRepository, never()).save(any(CreditCardData.class));
    }

    @Test
    void testGetCreditCardDataByOib_ExistingUser() throws CreditCardRegisterException {
        when(creditCardDataRepository.findByUserDataOib(anyLong())).thenReturn(Optional.of(creditCardData));

        UserCardRegisterResponse result = userCreditCardService.getCreditCardDataByOib(12345678901L);

        assertNotNull(result);
        assertEquals("last", result.getLastName());
    }

    @Test
    void testGetCreditCardDataByOib_NonExistingUser() {
        when(creditCardDataRepository.findByUserDataOib(anyLong())).thenReturn(Optional.empty());

        CreditCardRegisterException exception = assertThrows(CreditCardRegisterException.class, () -> {
            userCreditCardService.getCreditCardDataByOib(12345678901L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("404", exception.getCode());
        assertEquals("4004", exception.getId());
        assertEquals("User not found.", exception.getDescription());
    }

}
