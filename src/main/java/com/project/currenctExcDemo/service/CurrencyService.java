package com.project.currenctExcDemo.service;

import com.project.currenctExcDemo.model.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAllCurrencies();
    Currency save(Currency currency);
    Currency getCurrencyByCode(String code);
    Currency getCurrencyById(Long id);
}
