package com.project.currenctExcDemo.controller;

import com.project.currenctExcDemo.exceptions.CurrencyNotFoundExeption;
import com.project.currenctExcDemo.exceptions.InvalidInputExeption;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.ErrorModel;
import com.project.currenctExcDemo.service.ConvertJsonResp;
import com.project.currenctExcDemo.service.DBService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class GetCurrencyByCodeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().substring(1);
        ConvertJsonResp convertJsonResp = new ConvertJsonResp();
        DBService dbService = new DBService();
        Currency currency;
        if (code.length() != 3){
            resp.setStatus(400);
            convertJsonResp.convert(resp, new ErrorModel("CURRENCY CODE IS EMPTY"));
        }
        try {
            currency = dbService.getCurrencyByCode(code);
            convertJsonResp.convert(resp, currency);
        } catch (SQLException e) {
            resp.setStatus(500);
            convertJsonResp.convert(resp, new ErrorModel("There is problem with DataBase"));
        }
        catch (CurrencyNotFoundExeption e) {
            resp.setStatus(404);
            convertJsonResp.convert(resp, new ErrorModel(e.getMessage()));
        }
    }
}
