package com.project.currenctExcDemo.exceptions.handler;

import com.project.currenctExcDemo.exceptions.*;
import com.project.currenctExcDemo.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RateNotFoundException.class, CurrencyNotFoundExeption.class})
    public ResponseEntity<ErrorModel> handleNotFoundException(RateNotFoundException ex) {
        ErrorModel errorModel = new ErrorModel(ex.getMessage());
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CurrencyAlreadyExistFoundException.class, RateAlreadyExistFoundException.class})
    public ResponseEntity<ErrorModel> handleAlreadyExistFoundException(CurrencyAlreadyExistFoundException ex) {
        ErrorModel errorModel = new ErrorModel(ex.getMessage());
        return new ResponseEntity<>(errorModel, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidInputExeption.class)
    public ResponseEntity<ErrorModel> handleInvalidInputException(InvalidInputExeption ex) {
        ErrorModel errorModel = new ErrorModel(ex.getMessage());
        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

}