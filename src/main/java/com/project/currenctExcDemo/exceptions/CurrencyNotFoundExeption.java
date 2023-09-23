package com.project.currenctExcDemo.exceptions;

public class CurrencyNotFoundExeption extends RuntimeException{
    public CurrencyNotFoundExeption(String message) {
        super(message);
    }
}
