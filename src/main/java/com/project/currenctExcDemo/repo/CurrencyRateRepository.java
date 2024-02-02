package com.project.currenctExcDemo.repo;

import com.project.currenctExcDemo.model.Currency;
import com.project.currenctExcDemo.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> getCurrencyRateByBaseCurrency(Currency baseCurrency);

    Optional<CurrencyRate> getCurrencyRateByTargetCurrency(Currency targetCurrency);

    Optional<CurrencyRate> findByBaseCurrencyAndTargetCurrency(Currency baseCurrency, Currency targetCurrency);
}
