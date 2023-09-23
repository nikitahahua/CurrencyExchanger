Technology stack:

Servlets, Maven, Jackson, JDBC, PostreSQL,
___

Description of the exchanger:

REST API for describing currencies and exchange rates. Allows you to view and edit lists of currencies and exchange rates, and calculate the conversion of arbitrary amounts from one currency to another.

There is no web interface for the project, that is, using POSTMAN we can use all the functionality
___

API:


##### __GET `/currency`__

get all currencies from db, 
Sample answer:

```json
[
    "currency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },   
    "currency": {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

##### __GET `/currency/USD`__

get certain currency from db,
Sample answer:

```json
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

 __POST `/currency/`__

Adding a new currency to the database, we are sending data in JSON format
(in the CurrencyExchanger.postman file) 
respone:

```json
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

__Exchange Rates:__
__GET `/exchangeRates/`__
Getting a list of all exchange rates. Sample answer:

```json
[
    {
        "id": 1,
        "baseCurrency": {
            "id": 1,
            "code": "USD",
            "sign": "$",
            "fullName": "US Dollar"
        },
        "targetCurrency": {
            "id": 2,
            "code": "EUR",
            "sign": "?",
            "fullName": "Euro"
        },
        "rate": 0.940000
    },
    {
        "id": 2,
        "baseCurrency": {
            "id": 1,
            "code": "USD",
            "sign": "$",
            "fullName": "US Dollar"
        },
        "targetCurrency": {
            "id": 4,
            "code": "JPY",
            "sign": "¥",
            "fullName": "Yen"
        },
        "rate": 181.480000
    }
    ...
]
```

__GET `/exchangeRates/USDEUR`__
Getting a list of all exchange rates. Sample answer:

```json
[
    "id": 0,
    "exchangeRate": {
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.940000
    }
]
```

__GET `/exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT`__

Calculation of the transfer of a certain amount of funds from one currency to another.

Sample answer:

```json
{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00
    "convertedAmount": 14.50
}
```
Receiving an exchange rate can follow one of three scenarios. Let's say we make a transfer from currency A to currency B:

1. There is a currency pair AB in the ExchangeRates table - take its rate
2. There is a currency pair BA in the ExchangeRates table - take its rate and calculate the reverse to get AB
3. In the ExchangeRates table there are currency pairs USD-A and USD-B - we calculate the AB rate from these rates
##### __POST__ `/exchangeRates/`

Adding a new exchange rate to the database. Data is transmitted in the body of JSON file.

An example response is a JSON representation of a record inserted into the database, including its ID:

```json
{
     "id": 0,
     "baseCurrency": {
         "id": 0,
         "name": "United States dollar",
         "code": "USD",
         "sign": "$"
     },
     "targetCurrency": {
         "id": 1,
         "name": "Euro",
         "code": "EUR",
         "sign": "€"
     },
     "rate": 0.99
}
```
##### __PUT `/exchangeRates/USDEUR`__


Update the existing exchange rate in the database. The currency pair is specified by consecutive currency codes in the request address. Data is transmitted in the body of the request in the JSON file. The only form field is `rate`.

An example response is a JSON representation of the updated record in the database, including its ID:

```json
{
    "id": 1,
    "baseCurrency": {
        "id": 1,
        "code": "USD",
        "sign": "$",
        "fullName": "US Dollar"
    },
    "targetCurrency": {
        "id": 2,
        "code": "EUR",
        "sign": "?",
        "fullName": "Euro"
    },
    "rate": 1.4
}
```
___

For all requests, in case of an error, the response will look like this:

```json
{
     "message": "Currency not found"
}
```
