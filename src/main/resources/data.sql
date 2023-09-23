INSERT INTO Currencies (id, CODE, FullName, Sign) VALUES (1, 'USD', 'US Dollar', '$');
INSERT INTO Currencies (id, CODE, FullName, Sign) VALUES (2, 'EUR', 'Euro', '€');
INSERT INTO Currencies (id, CODE, FullName, Sign) VALUES (3, 'GBP', 'Pound Sterling', '£');
INSERT INTO Currencies (id, CODE, FullName, Sign) VALUES (4, 'JPY', 'Yen', '¥');

INSERT INTO ExchangeRates (id, BaseCurrencyId, TargetCurrencyId, Rate)
VALUES (1, 1, 2, 0.94);
INSERT INTO ExchangeRates (id, BaseCurrencyId, TargetCurrencyId, Rate)
VALUES (2, 1, 4, 148.28);
INSERT INTO ExchangeRates (id, BaseCurrencyId, TargetCurrencyId, Rate)
VALUES (3, 3, 4, 181.48);
INSERT INTO ExchangeRates (id, BaseCurrencyId, TargetCurrencyId, Rate)
VALUES (4, 4, 2, 0.0055);