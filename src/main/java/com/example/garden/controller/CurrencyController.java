package com.example.garden.controller;


import com.example.garden.model.Currency;
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
    public Currency addCurrency(@RequestBody Currency currency) {
       return currencyService.createCurrency(currency);
    }

    @PostMapping("/updateCurrency")
    public Currency updateCurrency(@RequestBody Currency currency) {
        return currencyService.updateCurrency(currency);
    }

    @DeleteMapping("/deleteCurrency/{id}")
    public void deleteCurrency(@PathVariable Integer id) {
        currencyService.deleteCurrency(id);
    }
    @GetMapping("/findCurrencyById/{id}")
    public Currency findCurrencyById(@PathVariable Integer id) {
        return currencyService.getCurrencyById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/findAllCurrencies")
    public Iterable<Currency> findAllCurrencies() {
        return currencyService.getAllCurrencies(Sort.by("name"));
    }

}
