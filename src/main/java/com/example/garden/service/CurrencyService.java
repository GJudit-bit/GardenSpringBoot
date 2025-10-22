package com.example.garden.service;

import com.example.garden.dto.CurrencyDto;
import com.example.garden.mapper.CurrencyMapper;
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
    private final CurrencyMapper currencyMapper;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        Currency currency = currencyMapper.toEntity(currencyDto);
        Currency savedCurrency = currencyRepository.save(currency);
        return currencyMapper.toCurrencyDto(savedCurrency);
    }

    public CurrencyDto findById(Integer id) {
        Currency currency= currencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Currency with id " + id + " does not exist."));
         return currencyMapper.toCurrencyDto(currency);
    }

    public CurrencyDto updateCurrency(CurrencyDto currencyDto) {
        Currency currency = currencyMapper.toEntity(currencyDto);
        Currency savedCurrency = currencyRepository.save(currency);
        return currencyMapper.toCurrencyDto(savedCurrency);
    }

    public void deleteCurrency(Integer id) {
        Currency currency = currencyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Currency with id " + id + " does not exist."));
        currencyRepository.deleteById(currency.getId());
    }

    public List<CurrencyDto> getAllCurrencies(Sort sort) {
        return currencyRepository.findAll().stream()
                .map(currencyMapper::toCurrencyDto)
                .toList();
    }
}
