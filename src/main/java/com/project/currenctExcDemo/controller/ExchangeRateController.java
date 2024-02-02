package com.project.currenctExcDemo.controller;

import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.payload.EnterRateRequest;
import com.project.currenctExcDemo.service.CurrencyRateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRateController {

    private final CurrencyRateService currencyRateService;

    public ExchangeRateController(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @GetMapping("/all")
    protected ResponseEntity<List<CurrencyRate>> getAllExchangeRates() {
        List<CurrencyRate> currencyRates = currencyRateService.getAllExchangeRates();
        return ResponseEntity.ok(currencyRates);
    }

    @GetMapping("/{pairCode}")
    protected ResponseEntity<CurrencyRate> getCurrencyRateByPairController(@PathVariable("pairCode") @Valid String pairCode) {
        CurrencyRate currencyRate = currencyRateService.getCertainExchangeRate(pairCode);
        return ResponseEntity.ok(currencyRate);
    }

    @PostMapping("/rate")
    protected ResponseEntity<CurrencyRate> postCurrencyRate(@RequestParam("baseCurrencyCode") String baseCurrencyCode
            , @RequestParam("targetCurrencyCode") String targetCurrencyCode
            , @RequestParam("rate") BigDecimal rate) {
        CurrencyRate currencyRate = currencyRateService.saveER(baseCurrencyCode,
                targetCurrencyCode, rate);
        return ResponseEntity.ok(currencyRate);
    }

    @PutMapping("/{pairCode}")
    protected ResponseEntity<CurrencyRate> putCurrencyExchange(@PathVariable("pairCode") @Valid String pairCode, @RequestBody EnterRateRequest rateRequest) {
        CurrencyRate currencyRate = currencyRateService.updateER(pairCode, rateRequest.getRate());
        return ResponseEntity.ok(currencyRate);
    }

}
