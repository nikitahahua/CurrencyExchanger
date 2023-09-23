package com.project.currenctExcDemo.service;

import com.project.currenctExcDemo.exceptions.CurrencyNotFoundExeption;
import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.model.ExchangeRateDTO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;

public class ConvertService {
    public ExchangeRateDTO exchangeCurrency(String baseCode, String targetCode, int amount) throws SQLException {
        CurrencyRate currencyRate = exchangeRate(baseCode , targetCode);
        return new ExchangeRateDTO(currencyRate, amount);
    }
    public CurrencyRate exchangeRate(String baseCode, String targetCode) throws SQLException {
        DBService dbService = new DBService();
        try {
            CurrencyRate currencyRate = dbService.getCertainExchangeRate(baseCode+targetCode);
            if (currencyRate != null){
                return currencyRate;
            }
            CurrencyRate currencyRate2 = dbService.getCertainExchangeRate(targetCode+baseCode);
            if (currencyRate2 != null){
                BigDecimal newRate = new BigDecimal("1").divide(currencyRate2.getRate(), new MathContext(4));
                currencyRate2.setRate(newRate);
                Currency currencyBase = currencyRate2.getBaseCurrency();
                currencyRate2.setBaseCurrency(currencyRate2.getTargetCurrency());
                currencyRate2.setTargetCurrency(currencyBase);
                return currencyRate2;
            }
            CurrencyRate currencyRate3 = dbService.getCertainExchangeRate("USD"+baseCode);
            CurrencyRate currencyRate4 = dbService.getCertainExchangeRate("USD"+targetCode);
            if (currencyRate3!=null && currencyRate4!=null){
                CurrencyRate currencyRateRes = new CurrencyRate();
                currencyRateRes.setBaseCurrency(dbService.getCurrencyByCode(baseCode));
                currencyRateRes.setTargetCurrency(dbService.getCurrencyByCode(targetCode));
                currencyRateRes.setRate(currencyRate3.getRate().divide(currencyRate4.getRate(), new MathContext(4)));
                return currencyRateRes;
            }
        }
        catch (CurrencyNotFoundExeption rr){
            throw new RateNotFoundException(rr.getMessage());
        }
        catch (SQLException sqlException){
            throw new SQLException("problem with db!");
        }
        return null;
    }
}
