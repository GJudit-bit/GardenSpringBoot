package com.example.garden.mapper;

import com.example.garden.dto.CurrencyDto;
import com.example.garden.model.Currency;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CurrencyMapper {

    public Currency toEntity(CurrencyDto currencyDto) {
        Currency currency = new Currency();
        currency.setId(currencyDto.id());
        currency.setName(currencyDto.name());
        return currency;
    }

    public CurrencyDto toCurrencyDto(Currency currency) {
        Integer id = currency.getId();
        String name = currency.getName();
        return new CurrencyDto(id, name);
    }

    public static List<CurrencyDto> toCurrencyDtoList(List<Currency> unitOfQuantities) {
        return unitOfQuantities.stream()
                .map(currency -> new CurrencyDto(currency.getId(), currency.getName()))
                .collect(Collectors.toList());
    }
}

