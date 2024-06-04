package com.example.credit_card_register.core.exception;

import com.example.credit_card_register.core.dto.BaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CreditCardRegisterException.class)
    public ResponseEntity<Object> handleCreditCardRegisterException(CreditCardRegisterException ex) {

        BaseDto responseBase = new BaseDto(ex.getStatus(), ex.getCode(), ex.getId(), ex.getDescription());
        return new ResponseEntity<>(responseBase, new HttpHeaders(), responseBase.getStatus());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatusCode status,
                                                                  final WebRequest request) {
        logException(ex);

        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final BaseDto responseBase = new BaseDto(HttpStatus.BAD_REQUEST, "400", "4006", "Validation failed", errors);
        return handleExceptionInternal(ex, responseBase, headers, responseBase.getStatus(), request);
    }


    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<BaseDto> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        logException(ex);
        final BaseDto errorResponse =  new BaseDto(HttpStatus.BAD_REQUEST, "400", "4007", "Input data invalid!");
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    private void logException(Throwable e) {
        log.error("Unhandled exception!", e);
    }
}
