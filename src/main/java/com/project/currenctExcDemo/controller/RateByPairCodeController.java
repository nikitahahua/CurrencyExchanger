package com.project.currenctExcDemo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.model.ErrorModel;
import com.project.currenctExcDemo.service.ConvertJsonResp;
import com.project.currenctExcDemo.service.DBService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/exchangeRates/*")
public class RateByPairCodeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().substring(1);
        DBService dbService = new DBService();
        ConvertJsonResp convertJsonResp = new ConvertJsonResp();
        CurrencyRate currencyRate;
        if (code.length()!=6){
            resp.setStatus(400);
            convertJsonResp.convert(resp, new ErrorModel("CODE PAIR IS EMPTY"));
            return;
        }
        try {
            currencyRate = dbService.getCertainExchangeRate(code);
            convertJsonResp.convert(resp, currencyRate);
        } catch (SQLException e) {
            resp.setStatus(500);
            convertJsonResp.convert(resp, new ErrorModel("Problem with DataBase"));
        }
        catch (RateNotFoundException ee){
            resp.setStatus(404);
            convertJsonResp.convert(resp, new ErrorModel(ee.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> jsonMap = objectMapper.readValue(req.getInputStream(), new TypeReference<Map<String, Object>>() {});
        ConvertJsonResp convertJsonResp = new ConvertJsonResp();
        String code = req.getPathInfo().substring(1);
        BigDecimal rate = (BigDecimal) jsonMap.get("rate");

        if (code.length()!=6){
            resp.setStatus(400);
            convertJsonResp.convert(resp, new ErrorModel("Pair Code is wrong"));
            return;
        }
        DBService dbService = new DBService();
        CurrencyRate currencyRate = null;
        try {
            currencyRate = dbService.updateER(code, rate);
            resp.setStatus(200);
        }
        catch (RateNotFoundException ee) {
            resp.setStatus(404);
            convertJsonResp.convert(resp, new ErrorModel(ee.getMessage()));
        }
        catch(SQLException e) {
            resp.setStatus(500);
            convertJsonResp.convert(resp, new ErrorModel("Problem with DataBase"));
        }
        convertJsonResp.convert(resp, currencyRate);
    }
}
