package com.project.currenctExcDemo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.model.ErrorModel;
import com.project.currenctExcDemo.service.ConvertJsonResp;
import com.project.currenctExcDemo.service.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/exchangeRates")
public class ExchangeRateController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBService dbService = new DBService();
        List<CurrencyRate> currencyRates;
        try {
            currencyRates = dbService.getAllExchangeRates();
        } catch (SQLException e) {
            resp.sendError(500, "Database error");
            throw new RuntimeException(e);
        }
        ConvertJsonResp convertJsonResp = new ConvertJsonResp();
        convertJsonResp.convert(resp, currencyRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DBService dbService = new DBService();
        Map<String, Object> jsonMap = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, Object>>() {});
        ConvertJsonResp convertJsonResp = new ConvertJsonResp();

        String baseCurrencyCode = (String) jsonMap.get("baseCurrencyCode");
        String targetCurrencyCode = (String) jsonMap.get("targetCurrencyCode");
        BigDecimal rate = (BigDecimal) jsonMap.get("rate");
        CurrencyRate currencyRate = null;

        try {
            currencyRate = new CurrencyRate(dbService.getCurrencyByCode(baseCurrencyCode),dbService.getCurrencyByCode(targetCurrencyCode),rate);
            if (currencyRate.getRate().equals(new BigDecimal(0))){
                resp.setStatus(400);
                convertJsonResp.convert(resp, new ErrorModel("RATE IS EMPTY"));
                return;
            }
            if (currencyRate.getBaseCurrency() == null){
                resp.setStatus(400);
                convertJsonResp.convert(resp, new ErrorModel("BaseCurrency IS EMPTY"));
                return;
            }
            if (currencyRate.getTargetCurrency() == null){
                resp.setStatus(400);
                convertJsonResp.convert(resp, new ErrorModel("TargetCurrency IS EMPTY"));
                return;
            }
            List<CurrencyRate> list = dbService.getAllExchangeRates();
            if (list.contains(currencyRate)){
                resp.setStatus(409);
                convertJsonResp.convert(resp, new ErrorModel("EXCHANGE RATE WITH THIS CODE ALREADY EXIST!"));
                return;
            }
            currencyRate = dbService.saveER(currencyRate.getBaseCurrency().getCode(),
                    currencyRate.getTargetCurrency().getCode(), currencyRate.getRate());
            resp.setStatus(200);
            convertJsonResp.convert(resp, currencyRate);
        } catch (SQLException e) {
            resp.setStatus(500);
            convertJsonResp.convert(resp, new ErrorModel("DB PROBLEM"));
        }
        catch (RateNotFoundException rr){
            resp.setStatus(404);
            convertJsonResp.convert(resp, rr.getMessage());
        }
    }
}
