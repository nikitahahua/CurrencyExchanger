INSERT INTO public.currencies (id, currency_code, Full_Name, currency_sign) VALUES (1, 'USD', 'US Dollar', '$');
INSERT INTO public.currencies (id, currency_code, Full_Name, currency_sign) VALUES (2, 'EUR', 'Euro', '€');
INSERT INTO public.currencies (id, currency_code, Full_Name, currency_sign) VALUES (3, 'GBP', 'Pound Sterling', '£');
INSERT INTO public.currencies (id, currency_code, Full_Name, currency_sign) VALUES (4, 'JPY', 'Yen', '¥');

INSERT INTO currency_rates (id, base_currency_id, target_currency_id, Rate)
VALUES (1, 1, 2, 0.94);
INSERT INTO ExchangeRates (id, base_currency_id, target_currency_id, Rate)
VALUES (2, 1, 4, 148.28);
INSERT INTO ExchangeRates (id, base_currency_id, target_currency_id, Rate)
VALUES (3, 3, 4, 181.48);
INSERT INTO ExchangeRates (id, base_currency_id, target_currency_id, Rate)
VALUES (4, 4, 2, 0.0055);