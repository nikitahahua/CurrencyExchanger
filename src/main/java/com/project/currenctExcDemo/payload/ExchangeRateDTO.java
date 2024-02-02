package com.project.currenctExcDemo.payload;


import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class
ExchangeRateDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private int amount;
    private BigDecimal convertedAmount;

    public ExchangeRateDTO(CurrencyRate currencyRate, int amount) {
        this.targetCurrency = currencyRate.getTargetCurrency();
        this.baseCurrency = currencyRate.getBaseCurrency();
        this.amount = amount;
        this.rate = currencyRate.getRate();
        convertedAmount = currencyRate.getRate().multiply(new BigDecimal(amount));
    }
}
