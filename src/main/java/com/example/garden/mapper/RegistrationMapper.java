package com.example.garden.mapper;

import com.example.garden.dto.RegistrationDto;
import com.example.garden.model.Registration;
import com.example.garden.service.CurrencyService;
import com.example.garden.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RegistrationMapper {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

   @Autowired
    public RegistrationMapper(ItemService itemService, ItemMapper itemMapper, CurrencyService currencyService, CurrencyMapper currencyMapper) {
        this.itemService = itemService;
       this.itemMapper = itemMapper;
       this.currencyService = currencyService;
       this.currencyMapper = currencyMapper;
   }


    public Registration toEntity(RegistrationDto registrationDto) {
        Registration registration = new Registration();
        registration.setId(registrationDto.id());
        registration.setItem(itemMapper.toEntity(itemService.findById(registrationDto.itemId())));
        registration.setCurrency(currencyMapper.toEntity(currencyService.findById(registrationDto.currencyId())));
        registration.setPrice(registrationDto.price());
        registration.setQuantity(registrationDto.quantity());
        registration.setExpense(registrationDto.expense());

        return registration;
    }

    public RegistrationDto toRegistrationDto(Registration registration) {
        return new RegistrationDto(registration.getId(), registration.getItem().getId(), registration.getCurrency().getId(), registration.getPrice(), registration.getQuantity(), registration.getExpense());
    }

    public List<RegistrationDto> toRegistrationDtoList(List<Registration> rewgistrations) {
        return rewgistrations.stream().map(registration -> new RegistrationDto(registration.getId(), registration.getItem().getId(), registration.getCurrency().getId(), registration.getPrice(), registration.getQuantity(), registration.getExpense())).collect(Collectors.toList());
    }
}
