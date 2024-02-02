package com.project.currenctExcDemo.service;

import com.project.currenctExcDemo.payload.ExchangeRateDTO;

public interface ConvertService {
    ExchangeRateDTO exchangeCurrency(String baseCode, String targetCode, int amount);
}
