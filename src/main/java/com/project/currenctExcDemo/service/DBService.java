package com.project.currenctExcDemo.service;

import com.project.currenctExcDemo.exceptions.CurrencyNotFoundExeption;
import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private static final String URL = "jdbc:postgresql://localhost:5432/mycurrency";
    private static final String PASSWORD = "root";
    private static final String USERNAME = "postgres";
    private static Connection connection;

    static{
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Currency> getAllCurrencies() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("SELECT * FROM Currencies");
        List<Currency> list = new ArrayList<>();
        while(resultSet.next()){
            Currency currency = new Currency(resultSet.getInt("id"),
                    resultSet.getString("code"), resultSet.getString("fullname"),
                    resultSet.getString("sign"));
            list.add(currency);
        }
        if(list.isEmpty()){
            throw new CurrencyNotFoundExeption("Currencies wasn't found in DataBase");
        }
        return list;
    }

    public Currency save(Currency currency) throws SQLException {
        List<Currency> list = getAllCurrencies();
        currency.setId(list.size()+1);
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement
                = connection.prepareStatement
                ("INSERT INTO currencies VALUES (?,?,?,?)");
        preparedStatement.setInt(1, currency.getId());
        preparedStatement.setString(2, currency.getCode());
        preparedStatement.setString(3, currency.getFullName());
        preparedStatement.setString(4, currency.getSign());

        preparedStatement.executeUpdate();
        return currency;
    }

    public Currency getCurrencyByCode(String code) throws SQLException {
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement
                = connection.prepareStatement
                ("SELECT * FROM currencies WHERE code=?");
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) { // Перемещаем указатель на первую запись
            int id = resultSet.getInt("id");
            String currencyCode = resultSet.getString("code");
            String fullName = resultSet.getString("fullName");
            String sign = resultSet.getString("sign");

            return new Currency(id, currencyCode, fullName, sign);
        } else {
            throw new CurrencyNotFoundExeption("Currency wasn't found in dataBase :(");
        }
    }
    public Currency getCurrencyById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement
                = connection.prepareStatement
                ("SELECT * FROM currencies WHERE id=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String currencyCode = resultSet.getString("code");
            String fullName = resultSet.getString("fullName");
            String sign = resultSet.getString("sign");

            return new Currency(id, currencyCode, fullName, sign);
        } else {
            throw new CurrencyNotFoundExeption("Currency wasn't found in dataBase :(");
        }
    }

    public List<CurrencyRate> getAllExchangeRates() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM exchangerates");
        List<CurrencyRate> list = new ArrayList<>();
        while(resultSet.next()){
            CurrencyRate currencyRate = new CurrencyRate(resultSet.getInt("id"),
                    getCurrencyById(resultSet.getInt("basecurrencyid")),
                    getCurrencyById(resultSet.getInt("targetcurrencyid")),
                    resultSet.getBigDecimal("rate"));
            list.add(currencyRate);
        }
        if (list.isEmpty()){
            throw new RateNotFoundException("there is no Exchange Rates in DataBase");
        }
        return list;
    }

    public CurrencyRate getCertainExchangeRate(String pair) throws SQLException {
        Statement statement = connection.createStatement();
        String secondPair = pair.substring(3,6);
        DBService dbService = new DBService();
        pair = pair.substring(0, 3);
        Currency currency = getCurrencyByCode(pair);
        Currency secondCurrency = getCurrencyByCode(secondPair);
        if (currency == null || secondCurrency == null){
            throw new RateNotFoundException("There is no Exchange rate, delivered wrong code pair");
        }
        CurrencyRate currencyRate = new CurrencyRate();
        PreparedStatement preparedStatement
                = connection.prepareStatement
                ("SELECT exchangerates.id, rate, c.code AS baseCode, c2.code FROM public.exchangerates\n" +
                        "\tJOIN  public.currencies c on c.id = basecurrencyid\n" +
                        "\tJOIN  public.currencies c2 on c2.id = targetcurrencyid\n" +
                        "WHERE c.code = ? AND c2.code = ?");
            preparedStatement.setString(1, pair);
            preparedStatement.setString(2, secondPair);
            ResultSet resultSet;
            try{
                resultSet = preparedStatement.executeQuery();
            }
            catch (Exception e){
                throw new CurrencyNotFoundExeption("CURRENCY WASNT FOUND");
            }
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String baseCur = resultSet.getString("baseCode");
            String targetCur = resultSet.getString("code");
            BigDecimal rate = resultSet.getBigDecimal("rate");

            return new CurrencyRate(id, dbService.getCurrencyByCode(baseCur), dbService.getCurrencyByCode(targetCur), rate);
        } else {
            return null;
        }
    }

    public CurrencyRate saveER(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException {
        Currency currency = getCurrencyByCode(baseCurrencyCode);
        Currency secondCurrency = getCurrencyByCode(targetCurrencyCode);

        CurrencyRate newCurencyRate = new CurrencyRate(currency, secondCurrency, rate);
        List<CurrencyRate> list = getAllExchangeRates();
        newCurencyRate.setId(list.size()+1);

        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement
                = connection.prepareStatement
                ("INSERT INTO exchangerates (id, basecurrencyid, targetcurrencyid, rate) VALUES (?,?,?,?)");
        preparedStatement.setInt(1, newCurencyRate.getId());
        preparedStatement.setInt(2, currency.getId());
        preparedStatement.setInt(3, secondCurrency.getId());
        preparedStatement.setBigDecimal(4, rate);

        preparedStatement.executeUpdate();
        return newCurencyRate;
    }

    public CurrencyRate updateER(String pair, BigDecimal rate) throws SQLException {
        CurrencyRate currencyRateRes = null;
        try{
            currencyRateRes = getCertainExchangeRate(pair);
        }
        catch (RuntimeException rr){
            throw new RateNotFoundException("There is no Exchange rate, delivered wrong code pair");
        }
        currencyRateRes.setRate(rate);
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement
                = connection.prepareStatement
                ("UPDATE exchangerates SET rate = ? WHERE id = ?");
        preparedStatement.setBigDecimal(1, rate);
        preparedStatement.setInt(2, currencyRateRes.getId());
        preparedStatement.executeUpdate();

        return currencyRateRes;
    }


}
