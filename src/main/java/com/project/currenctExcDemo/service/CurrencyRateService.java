package com.project.currenctExcDemo.service;

import com.project.currenctExcDemo.model.CurrencyRate;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyRateService {
    List<CurrencyRate> getAllExchangeRates();
    CurrencyRate getCertainExchangeRate(String pair);
    CurrencyRate saveER(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);
    CurrencyRate updateER(String pair, BigDecimal rate);
}
