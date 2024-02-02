package unit;

import com.project.currenctExcDemo.exceptions.CurrencyAlreadyExistFoundException;
import com.project.currenctExcDemo.exceptions.CurrencyNotFoundExeption;
import com.project.currenctExcDemo.exceptions.InvalidInputExeption;
import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.repo.CurrencyRepository;
import com.project.currenctExcDemo.service.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Test
    void getAllCurrenciesReturnsNonEmptyList() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency("USD", "$", "US Dollar"));
        currencies.add(new Currency("EUR", "€", "Euro"));
        when(currencyRepository.findAll()).thenReturn(currencies);
        List<Currency> result = currencyService.getAllCurrencies();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(currencies, result);
    }

    @Test
    void getAllCurrenciesThrowsExceptionWhenEmpty() {
        when(currencyRepository.findAll()).thenReturn(new ArrayList<>());
        CurrencyNotFoundExeption exception = assertThrows(CurrencyNotFoundExeption.class, () -> currencyService.getAllCurrencies());
        assertEquals("Currencies wasn't found in DataBase", exception.getMessage());
    }

    @Test
    void testGetAllCurrenciesThrowsException() {
        when(currencyRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(CurrencyNotFoundExeption.class, () -> currencyService.getAllCurrencies());
    }

    @Test
    void testSuccessSaveCurrency() {
        Currency currency = new Currency("USD", "$", "US Dollar");
        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("TES", "T", " TEST ")));
        when(currencyRepository.save(currency)).thenReturn(currency);
        Currency result = currencyService.save(currency);
        assertEquals(currency, result);
        verify(currencyRepository, times(1)).save(currency);
    }

    @Test
    void testSaveCurrencyThrowsInvalidInputException() {
        Currency currency = new Currency(null, "$", "US Dollar");
        assertThrows(InvalidInputExeption.class, () -> currencyService.save(currency));
    }

    @Test
    void testSaveCurrencyThrowsCurrencyAlreadyExistException() {
        Currency currency = new Currency("USD", "$", "US Dollar");
        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("USD", "$", "US Dollar")));
        assertThrows(CurrencyAlreadyExistFoundException.class, () -> currencyService.save(currency));
    }

    @Test
    void testSuccessGetCurrencyByCode() {
        String code = "USD";
        Currency currency = new Currency(code, "$", "US Dollar");
        when(currencyRepository.findCurrencyByCode(code)).thenReturn(Optional.of(currency));

        Currency result = currencyService.getCurrencyByCode(code);
        assertEquals(currency, result);
    }

    @Test
    void testGetCurrencyByCodeThrowsCurrencyNotFoundException() {
        String code = "USD";
        when(currencyRepository.findCurrencyByCode(code)).thenReturn(Optional.empty());
        assertThrows(CurrencyNotFoundExeption.class, () -> currencyService.getCurrencyByCode(code));
    }

    @Test
    void testSuccessGetCurrencyById() {
        Long id = 1L;
        Currency currency = new Currency("USD", "$", "US Dollar");
        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));
        Currency result = currencyService.getCurrencyById(id);
        assertEquals(currency, result);
    }

    @Test
    void testGetCurrencyByIdThrowsCurrencyNotFoundException() {
        Long id = 1L;
        when(currencyRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CurrencyNotFoundExeption.class, () -> currencyService.getCurrencyById(id));
    }
}