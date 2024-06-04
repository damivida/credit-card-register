package com.example.credit_card_register.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDto {

   private HttpStatus status = HttpStatus.OK;
   private String code;
   private String id;
   private String description;
   private List<String> errors;



    public BaseDto(HttpStatus status) {
        this.status = status;
    }

    public BaseDto(HttpStatus status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }

    public BaseDto(HttpStatus status, String code, String id, String description) {
        this.status = status;
        this.code = code;
        this.id = id;
        this.description = description;
    }

    public BaseDto(HttpStatus status, String code, String id, String description, List<String> errors) {
        this.status = status;
        this.code = code;
        this.id = id;
        this.description = description;
        this.errors = errors;
    }
}
