package unit;

import com.project.currenctExcDemo.exceptions.RateAlreadyExistFoundException;
import com.project.currenctExcDemo.exceptions.RateNotFoundException;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;
import com.project.currenctExcDemo.repo.CurrencyRateRepository;
import com.project.currenctExcDemo.service.CurrencyService;
import com.project.currenctExcDemo.service.impl.CurrencyRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceImplTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    private CurrencyRateServiceImpl currencyRateService;


    @Test
    void testGetAllExchangeRates() {
        List<CurrencyRate> rates = new ArrayList<>();
        rates.add(new CurrencyRate(new Currency("USD", "$", "US Dollar"), new Currency("EUR", "€", "Euro"), BigDecimal.valueOf(1.2)));
        rates.add(new CurrencyRate(new Currency("USD", "$", "US Dollar"), new Currency("GBP", "£", "British Pound"), BigDecimal.valueOf(0.8)));
        when(currencyRateRepository.findAll()).thenReturn(rates);
        List<CurrencyRate> result = currencyRateService.getAllExchangeRates();
        assertEquals(rates, result);
    }

    @Test
    void testGetAllExchangeRatesThrowsException() {
        when(currencyRateRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(RateNotFoundException.class, () -> currencyRateService.getAllExchangeRates());
    }

    @Test
    void testGetCertainExchangeRate() {
        String pair = "USDGBP";
        Currency baseCurrency = new Currency("USD", "$", "US Dollar");
        Currency targetCurrency = new Currency("GBP", "£", "British Pound");
        CurrencyRate currencyRate = new CurrencyRate(baseCurrency, targetCurrency, BigDecimal.valueOf(0.8));
        when(currencyService.getCurrencyByCode("USD")).thenReturn(baseCurrency);
        when(currencyService.getCurrencyByCode("GBP")).thenReturn(targetCurrency);
        when(currencyRateRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)).thenReturn(Optional.of(currencyRate));
        CurrencyRate result = currencyRateService.getCertainExchangeRate(pair);
        assertEquals(currencyRate, result);
    }

    @Test
    void testGetCertainExchangeRateThrowsIllegalArgumentException() {
        String pair = "USDE";
        assertThrows(IllegalArgumentException.class, () -> currencyRateService.getCertainExchangeRate(pair));
    }

    @Test
    void testGetCertainExchangeRateThrowsRateNotFoundException() {
        String pair = "USDGBP";
        Currency baseCurrency = new Currency("USD", "$", "US Dollar");
        Currency targetCurrency = new Currency("GBP", "£", "British Pound");
        when(currencyService.getCurrencyByCode("USD")).thenReturn(baseCurrency);
        when(currencyService.getCurrencyByCode("GBP")).thenReturn(targetCurrency);
        when(currencyRateRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)).thenReturn(Optional.empty());

        assertThrows(RateNotFoundException.class, () -> currencyRateService.getCertainExchangeRate(pair));
    }

    @Test
    void testSaveER() {
        String baseCurrencyCode = "USD";
        String targetCurrencyCode = "EUR";
        BigDecimal rate = BigDecimal.valueOf(1.2);
        Currency baseCurrency = new Currency(baseCurrencyCode, "$", "US Dollar");
        Currency targetCurrency = new Currency(targetCurrencyCode, "€", "Euro");
        CurrencyRate currencyRate = new CurrencyRate(baseCurrency, targetCurrency, rate);
        when(currencyService.getCurrencyByCode(baseCurrencyCode)).thenReturn(baseCurrency);
        when(currencyService.getCurrencyByCode(targetCurrencyCode)).thenReturn(targetCurrency);
        when(currencyRateRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)).thenReturn(Optional.empty());
        when(currencyRateRepository.save(currencyRate)).thenReturn(currencyRate);

        CurrencyRate result = currencyRateService.saveER(baseCurrencyCode, targetCurrencyCode, rate);

        assertEquals(currencyRate, result);
    }

    @Test
    void testSaveERThrowsRateNotFoundException() {
        // Arrange
        String baseCurrencyCode = "USD";
        String targetCurrencyCode = "EUR";
        BigDecimal rate = BigDecimal.valueOf(1.2);
        Currency baseCurrency = new Currency(baseCurrencyCode, "$", "US Dollar");
        Currency targetCurrency = new Currency(targetCurrencyCode, "€", "Euro");
        CurrencyRate existingRate = new CurrencyRate(baseCurrency, targetCurrency, BigDecimal.valueOf(1.3));
        when(currencyService.getCurrencyByCode(baseCurrencyCode)).thenReturn(baseCurrency);
        when(currencyService.getCurrencyByCode(targetCurrencyCode)).thenReturn(targetCurrency);
        when(currencyRateRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)).thenReturn(Optional.of(existingRate));

        assertThrows(RateAlreadyExistFoundException.class, () -> currencyRateService.saveER(baseCurrencyCode, targetCurrencyCode, rate));
    }

    @Test
    void testUpdateER() {
        String pair = "USDGBP";
        BigDecimal newRate = BigDecimal.valueOf(0.8);
        Currency baseCurrency = new Currency("USD", "$", "US Dollar");
        Currency targetCurrency = new Currency("GBP", "£", "British Pound");
        CurrencyRate existingRate = new CurrencyRate(baseCurrency, targetCurrency, BigDecimal.valueOf(0.75));

        when(currencyService.getCurrencyByCode("USD")).thenReturn(baseCurrency);
        when(currencyService.getCurrencyByCode("GBP")).thenReturn(targetCurrency);
        when(currencyRateRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)).thenReturn(Optional.of(existingRate));
        when(currencyRateRepository.save(any(CurrencyRate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CurrencyRate result = currencyRateService.updateER(pair, newRate);

        assertNotNull(result);
        assertEquals(newRate, result.getRate());
        verify(currencyRateRepository).save(existingRate);
    }

    @Test
    void testUpdateERThrowsIllegalArgumentException() {
        String pair = "USD";
        BigDecimal rate = BigDecimal.valueOf(0.8);

        assertThrows(IllegalArgumentException.class, () -> currencyRateService.updateER(pair, rate));
    }

    @Test
    void testUpdateERThrowsRateNotFoundException() {
        String pair = "USDGBP";
        Currency existingBaseRate = new Currency("USD", "$", "US Dollar");
        Currency existingTargetBaseRate = new Currency("GBP", "£", "British Pound");
        BigDecimal rate = BigDecimal.valueOf(0.8);
        CurrencyRate existingRate = new CurrencyRate(new Currency("USD", "$", "US Dollar"), new Currency("GBP", "£", "British Pound"), BigDecimal.valueOf(0.8));
        currencyService.getCurrencyByCode("USD");
        when(currencyService.getCurrencyByCode("USD")).thenReturn(existingBaseRate);
        when(currencyService.getCurrencyByCode("GBP")).thenReturn(existingTargetBaseRate);
        when(currencyRateRepository.findByBaseCurrencyAndTargetCurrency(existingBaseRate, existingTargetBaseRate)).thenReturn(Optional.of(existingRate));
        when(currencyRateService.getCertainExchangeRate(pair)).thenThrow(new RateNotFoundException("Exchange rate not found"));
        assertThrows(RateNotFoundException.class, () -> currencyRateService.updateER(pair, rate));
    }
}