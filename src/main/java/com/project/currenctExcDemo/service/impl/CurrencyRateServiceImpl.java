package com.project.currenctExcDemo.service.impl;

import com.project.currenctExcDemo.exceptions.RateAlreadyExistFoundException;
import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.repo.CurrencyRateRepository;
import com.project.currenctExcDemo.service.CurrencyRateService;
import com.project.currenctExcDemo.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyService currencyService;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRateServiceImpl(CurrencyService currencyService, CurrencyRateRepository currencyRateRepository) {
        this.currencyService = currencyService;
        this.currencyRateRepository = currencyRateRepository;
    }

    @Override
    public List<CurrencyRate> getAllExchangeRates() {
        List<CurrencyRate> allRates = currencyRateRepository.findAll();
        if (allRates.isEmpty()) {
            throw new RateNotFoundException("Exchange Rates were not found in DataBase");
        }
        return allRates;
    }

    @Override
    public CurrencyRate getCertainExchangeRate(String pair) {
        if (pair == null || pair.length() < 6) {
            throw new IllegalArgumentException("Pair must be 6 characters long");
        }

        String secondPair = pair.substring(3, 6);
        pair = pair.substring(0, 3);
        Currency currency = currencyService.getCurrencyByCode(pair);
        Currency secondCurrency = currencyService.getCurrencyByCode(secondPair);
        if (currency == null || secondCurrency == null) {
            throw new RateNotFoundException("There is no Exchange rate, delivered wrong code pair");
        }
        String finalPair = pair;
        if (currencyRateRepository.findByBaseCurrencyAndTargetCurrency(currency, secondCurrency).isPresent()){
            return currencyRateRepository.findByBaseCurrencyAndTargetCurrency(currency, secondCurrency).get();
        }
        else{
            return null;
        }
    }

    @Override
    public CurrencyRate saveER(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            throw new RateNotFoundException("There is no Exchange rate, delivered wrong code pair");
        }
        Currency baseCurrency = currencyService.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.getCurrencyByCode(targetCurrencyCode);

        Optional<CurrencyRate> tempRate = currencyRateRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
        if (tempRate.isPresent()) {
            throw new RateAlreadyExistFoundException("rate : " + baseCurrencyCode + targetCurrencyCode + " already exist");
        }

        CurrencyRate newCurrencyRate = new CurrencyRate(baseCurrency, targetCurrency, rate);
        currencyRateRepository.save(newCurrencyRate);
        return newCurrencyRate;
    }

    @Override
    public CurrencyRate updateER(String pair, BigDecimal rate) {
        if (pair == null || pair.length() < 6 || rate == null) {
            throw new IllegalArgumentException("Invalid input for updating exchange rate.");
        }
        try{
            CurrencyRate currencyRateRes = getCertainExchangeRate(pair);
            currencyRateRes.setRate(rate);
            currencyRateRepository.save(currencyRateRes);
            return currencyRateRes;
        }
        catch (RuntimeException rr){
            throw new RateNotFoundException(rr.getMessage());
        }
    }

}
