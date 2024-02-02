package com.project.currenctExcDemo.service.impl;

import com.project.currenctExcDemo.exceptions.CurrencyNotFoundExeption;
import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.payload.ExchangeRateDTO;
import com.project.currenctExcDemo.service.ConvertService;
import com.project.currenctExcDemo.service.CurrencyRateService;
import com.project.currenctExcDemo.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;

@Service
public class ConvertServiceImpl implements ConvertService {

    private final CurrencyRateService currencyRateService;
    private final CurrencyService currencyService;

    public ConvertServiceImpl(CurrencyRateService currencyRateService, CurrencyService currencyService) {
        this.currencyRateService = currencyRateService;
        this.currencyService = currencyService;
    }

    @Override
    public ExchangeRateDTO exchangeCurrency(String baseCode, String targetCode, int amount) {
        CurrencyRate currencyRate = exchangeRate(baseCode, targetCode);
        return new ExchangeRateDTO(currencyRate, amount);
    }

    private CurrencyRate exchangeRate(String baseCode, String targetCode) {
        try {
            CurrencyRate currencyRate = currencyRateService.getCertainExchangeRate(baseCode + targetCode);
            if (currencyRate != null) {
                return currencyRate;
            }
            CurrencyRate currencyRate2 = currencyRateService.getCertainExchangeRate(targetCode + baseCode);
            if (currencyRate2 != null) {
                BigDecimal newRate = new BigDecimal("1").divide(currencyRate2.getRate(), new MathContext(4));
                currencyRate2.setRate(newRate);
                Currency currencyBase = currencyRate2.getBaseCurrency();
                currencyRate2.setBaseCurrency(currencyRate2.getTargetCurrency());
                currencyRate2.setTargetCurrency(currencyBase);
                return currencyRate2;
            }
            CurrencyRate currencyRate3 = currencyRateService.getCertainExchangeRate("USD" + baseCode);
            CurrencyRate currencyRate4 = currencyRateService.getCertainExchangeRate("USD" + targetCode);
            if (currencyRate3 != null && currencyRate4 != null) {
                CurrencyRate currencyRateRes = new CurrencyRate();
                currencyRateRes.setBaseCurrency(currencyService.getCurrencyByCode(baseCode));
                currencyRateRes.setTargetCurrency(currencyService.getCurrencyByCode(targetCode));
                currencyRateRes.setRate(currencyRate3.getRate().divide(currencyRate4.getRate(), new MathContext(4)));
                return currencyRateRes;
            }
            else{
                throw new RateNotFoundException("cant find provided rate: "+ baseCode + " targetCode: " + targetCode);
            }
        } catch (CurrencyNotFoundExeption rr) {
            throw new CurrencyNotFoundExeption(rr.getMessage());
        }
    }
}
