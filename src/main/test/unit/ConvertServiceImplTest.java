package unit;

import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.payload.ExchangeRateDTO;
import com.project.currenctExcDemo.service.CurrencyRateService;
import com.project.currenctExcDemo.service.CurrencyService;
import com.project.currenctExcDemo.service.impl.ConvertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConvertServiceImplTest {

    @Mock
    private CurrencyRateService currencyRateService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private ConvertServiceImpl convertService;

    @Test
    void testExchangeCurrency() {
        String baseCode = "USD";
        String targetCode = "EUR";
        int amount = 100;
        CurrencyRate currencyRate = new CurrencyRate(new Currency(baseCode, "$", "US Dollar"),
                new Currency(targetCode, "€", "Euro"), BigDecimal.valueOf(0.8));

        when(currencyRateService.getCertainExchangeRate(baseCode + targetCode)).thenReturn(currencyRate);

        ExchangeRateDTO result = convertService.exchangeCurrency(baseCode, targetCode, amount);

        BigDecimal expectedResultAmount = BigDecimal.valueOf(80.0);
        assertEquals(expectedResultAmount, result.getConvertedAmount());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void testExchangeCurrencyHandlesReverseRate() {
        String baseCode = "USD";
        String targetCode = "EUR";
        int amount = 100;
        CurrencyRate currencyRate = new CurrencyRate(new Currency(targetCode, "$", "US Dollar"),
                new Currency(baseCode, "€", "Euro"), BigDecimal.valueOf(1.2));
        when(currencyRateService.getCertainExchangeRate(targetCode + baseCode)).thenReturn(currencyRate);

        ExchangeRateDTO result = convertService.exchangeCurrency(targetCode, baseCode, amount);

        BigDecimal expectedResultAmount = BigDecimal.valueOf(120.0);

        assertNotNull(result);
        assertEquals(expectedResultAmount, result.getConvertedAmount());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void testExchangeCurrencyHandlesUSDConversion() {
        String baseCode = "GBP";
        String targetCode = "EUR";
        int amount = 100;

        CurrencyRate currencyRate1 = new CurrencyRate(new Currency("USD", "$", "US Dollar"),
                new Currency(baseCode, "£", "British Pound"),
                BigDecimal.valueOf(0.8));
        CurrencyRate currencyRate2 = new CurrencyRate(new Currency("USD", "$", "US Dollar"),
                new Currency(targetCode, "€", "Euro"),
                BigDecimal.valueOf(1.2));

        when(currencyRateService.getCertainExchangeRate(baseCode + targetCode)).thenReturn(null);
        when(currencyRateService.getCertainExchangeRate(targetCode + baseCode)).thenReturn(null);

        when(currencyRateService.getCertainExchangeRate("USD" + baseCode)).thenReturn(currencyRate1);
        when(currencyRateService.getCertainExchangeRate("USD" + targetCode)).thenReturn(currencyRate2);

        when(currencyService.getCurrencyByCode(baseCode)).thenReturn(new Currency(baseCode, "£", "British Pound"));
        when(currencyService.getCurrencyByCode(targetCode)).thenReturn(new Currency(targetCode, "€", "Euro"));

        ExchangeRateDTO result = convertService.exchangeCurrency(baseCode, targetCode, amount);

        assertNotNull(result);
        assertEquals(amount, result.getAmount());
        BigDecimal expectedRate = currencyRate1.getRate().divide(currencyRate2.getRate(), new MathContext(4));
        assertEquals(expectedRate, result.getRate());
    }

    @Test
    void testExchangeCurrencyThrowsRateNotFoundException() throws SQLException {
        String baseCode = "USD";
        String targetCode = "EUR";
        int amount = 100;

        assertThrows(RateNotFoundException.class, () -> convertService.exchangeCurrency(baseCode, targetCode, amount));
    }
}