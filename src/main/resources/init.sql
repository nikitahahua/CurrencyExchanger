CREATE TABLE IF NOT EXISTS Currencies (
    ID serial PRIMARY KEY,
    CODE varchar(255) UNIQUE NOT NULL,
    FullName varchar(255) NOT NULL,
    Sign varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS ExchangeRates (
    ID serial PRIMARY KEY,
    BaseCurrencyId serial REFERENCES Currencies (ID) NOT NULL,
    TargetCurrencyId serial REFERENCES Currencies (ID) NOT NULL,
    Rate Decimal(10,6) NOT NULL
);