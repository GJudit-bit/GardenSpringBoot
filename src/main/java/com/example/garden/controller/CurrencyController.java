package com.example.garden.controller;


import com.example.garden.dto.CurrencyDto;
import com.example.garden.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@EnableWebMvc
@RequestMapping("/garden")
public class    CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/addCurrency")
    public CurrencyDto create(@RequestBody CurrencyDto currencyDto) {
       return currencyService.createCurrency(currencyDto);
    }

    @PostMapping("/updateCurrency")
    public CurrencyDto updateCurrency(@RequestBody CurrencyDto currencyDto) {
        return currencyService.updateCurrency(currencyDto);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/deleteCurrency/{id}")
    public void deleteCurrency(@PathVariable Integer id) {
        currencyService.deleteCurrency(id);
    }
    @GetMapping("/findCurrencyById/{id}")
    public CurrencyDto findById(@PathVariable Integer id) {
        return currencyService.findById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/findAllCurrencies")
    public Iterable<CurrencyDto> findAllCurrencies() {
        return currencyService.getAllCurrencies(Sort.by("name"));
    }

}
