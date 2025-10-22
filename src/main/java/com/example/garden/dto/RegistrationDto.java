package com.example.garden.dto;

public record RegistrationDto(Integer id, Integer itemId, Integer currencyId, Double price, Double quantity, Boolean expense) {
}
