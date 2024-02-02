package com.project.currenctExcDemo.controller;

import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.payload.UploadCurrencyRequest;
import com.project.currenctExcDemo.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    protected ResponseEntity<List<Currency>> getAllCurrencies() {
        List<Currency> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/{currency_code}")
    protected ResponseEntity<Currency> getCurrencyByCodeController(@PathVariable("currency_code") @Valid String currencyCode) {
        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        return ResponseEntity.ok(currency);
    }

    @PostMapping
    protected ResponseEntity<Currency> uploadCurrency(@RequestBody UploadCurrencyRequest currencyRequest) {
        Currency currency = new Currency(currencyRequest.getCode(), currencyRequest.getName(), currencyRequest.getSign());
        currencyService.save(currency);
        return ResponseEntity.ok(currency);
    }

}
