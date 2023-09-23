package com.project.currenctExcDemo.controller;

import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.ErrorModel;
import com.project.currenctExcDemo.model.ExchangeRateDTO;
import com.project.currenctExcDemo.service.ConvertJsonResp;
import com.project.currenctExcDemo.service.ConvertService;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exchange")
public class ConvertAmoutController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fromCurrencyCode = request.getParameter("from");
        String toCurrencyCode = request.getParameter("to");
        int amount = Integer.parseInt(request.getParameter("amount"));

        ConvertService convertService = new ConvertService();

        ConvertJsonResp convertJsonResp = new ConvertJsonResp();

        ExchangeRateDTO dto = null;
        try {
            dto = convertService.exchangeCurrency(fromCurrencyCode, toCurrencyCode, amount);
            convertJsonResp.convert(response, dto);
        } catch (SQLException e) {
            response.setStatus(500);
            convertJsonResp.convert(response, new ErrorModel("There is problem with DataBase"));
        }
        catch (RateNotFoundException ee){
            response.setStatus(404);
            convertJsonResp.convert(response, new ErrorModel(ee.getMessage()));
        }
    }
}
