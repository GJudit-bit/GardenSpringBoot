package com.example.garden.service;

import com.example.garden.model.Currency;
import com.example.garden.repository.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Currency createCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public Currency getCurrencyById(Integer id) {
        return currencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Currency with id " + id + " does not exist."));
    }

    public Currency updateCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public void deleteCurrency(Integer id) {
        Currency currency = currencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Currency with id " + id + " does not exist."));
        currencyRepository.deleteById(currency.getId());
    }

    public List<Currency> getAllCurrencies(Sort sort) {
        return currencyRepository.findAll();
    }
}
