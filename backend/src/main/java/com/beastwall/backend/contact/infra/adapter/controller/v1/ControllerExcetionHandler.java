package com.beastwall.backend.contact.infra.adapter.controller.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExcetionHandler {

    @ExceptionHandler(Exception.class)
    // TODO: treat EACH exception separately with proper response
    public ResponseEntity<String> handleAllExceptions(){
        return ResponseEntity.badRequest().body("Bad Request / Bad parameters, the api will be updated soon to give you more details");
    }
}
