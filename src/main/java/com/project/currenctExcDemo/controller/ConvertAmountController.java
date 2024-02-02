package com.project.currenctExcDemo.controller;

import com.project.currenctExcDemo.payload.ExchangeRateDTO;
import com.project.currenctExcDemo.service.ConvertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class ConvertAmountController {

    private final ConvertService convertService;

    public ConvertAmountController(ConvertService convertService) {
        this.convertService = convertService;
    }

    @GetMapping("/")
    protected ResponseEntity<ExchangeRateDTO> convertAmount(@RequestParam String from, @RequestParam String to, @RequestParam Integer amount) {
        ExchangeRateDTO dto = convertService.exchangeCurrency(from, to, amount);
        return ResponseEntity.ok(dto);
    }

}
