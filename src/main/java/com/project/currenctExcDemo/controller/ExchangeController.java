package com.project.currenctExcDemo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currenctExcDemo.exceptions.CurrencyNotFoundExeption;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.ErrorModel;
import com.project.currenctExcDemo.service.ConvertJsonResp;
import com.project.currenctExcDemo.service.DBService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/currency")
public class ExchangeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBService dbService = new DBService();
        ConvertJsonResp convertJsonResp = new ConvertJsonResp();
        List<Currency> currencies;
        try {
            currencies = dbService.getAllCurrencies();
            convertJsonResp.convert(resp, currencies);
        }
        catch (SQLException e) {
            resp.setStatus(500);
            convertJsonResp.convert(resp, new ErrorModel("Database error"));
        }
        catch (CurrencyNotFoundExeption ee){
            resp.setStatus(404);
            convertJsonResp.convert(resp, new ErrorModel(ee.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> jsonMap = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, Object>>() {});
        ConvertJsonResp convertJsonResp = new ConvertJsonResp();

        String name = (String) jsonMap.get("name");
        String code = (String) jsonMap.get("code");
        String sign = (String) jsonMap.get("sign");
        Currency currency = new Currency(code,name,sign);
        if (currency.getCode() == null){
            resp.setStatus(400);
            convertJsonResp.convert(resp, new ErrorModel("CODE IS EMPTY"));
            return;
        }
        if (currency.getFullName() == null){
            resp.setStatus(400);
            convertJsonResp.convert(resp, new ErrorModel("fullName is empty"));
            return;
        }
        if (currency.getSign() == null){
            resp.setStatus(400);
            convertJsonResp.convert(resp, new ErrorModel("Sign is empty"));
            return;
        }
        DBService dbService = new DBService();
        try {
            List<Currency> list = dbService.getAllCurrencies();
            if (list.contains(currency)){
                resp.setStatus(409);
                convertJsonResp.convert(resp, new ErrorModel("CURRENCY WITH THIS CODE ALREADY EXIST!"));
                return;
            }
            currency = dbService.save(currency);
            resp.setStatus(200);
            convertJsonResp.convert(resp, currency);
        } catch (SQLException e) {
            resp.setStatus(500);
            convertJsonResp.convert(resp, new ErrorModel("Problem with DataBase"));
        }


    }
}
