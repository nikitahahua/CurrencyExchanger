package com.project.currenctExcDemo.service.impl;

import com.project.currenctExcDemo.exceptions.CurrencyAlreadyExistFoundException;
import com.project.currenctExcDemo.exceptions.CurrencyNotFoundExeption;
import com.project.currenctExcDemo.exceptions.InvalidInputExeption;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.repo.CurrencyRepository;
import com.project.currenctExcDemo.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> allCurrencies = currencyRepository.findAll();
        if (allCurrencies.isEmpty()) {
            throw new CurrencyNotFoundExeption("Currencies wasn't found in DataBase");
        }
        return allCurrencies;
    }

    @Override
    public Currency save(Currency currency) {
        if (currency.getCode() == null || currency.getCode().isEmpty() ||
                currency.getSign() == null || currency.getSign().isEmpty() ||
                currency.getFullName() == null || currency.getFullName().isEmpty()) {

            throw new InvalidInputExeption("wrong currency information was provided");
        } else if (getAllCurrencies().contains(currency)) {
            throw new CurrencyAlreadyExistFoundException("this currency already exist: \t " + currency.getFullName());
        }

        currencyRepository.save(currency);
        return currency;
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        Optional<Currency> findCurrency = currencyRepository.findCurrencyByCode(code);
        if (findCurrency.isPresent()) {
            return findCurrency.get();
        } else {
            throw new CurrencyNotFoundExeption("Cant find currency by code: " + code);
        }
    }

    @Override
    public Currency getCurrencyById(Long id) {
        Optional<Currency> findCurrency = currencyRepository.findById(id);
        if (findCurrency.isPresent()) {
            return findCurrency.get();
        } else {
            throw new CurrencyNotFoundExeption("Cant find currency by id: " + id);
        }
    }
}
