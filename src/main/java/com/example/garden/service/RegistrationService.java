package com.example.garden.service;

import com.example.garden.dto.RegistrationDto;
import com.example.garden.mapper.RegistrationMapper;
import com.example.garden.model.Registration;
import com.example.garden.model.ExpenseSummary;
import com.example.garden.repository.RegistrationRepository;

import com.example.garden.utility.DateUtility;
import com.example.garden.utility.UserUtility;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
    }


    public RegistrationDto createRegistration(RegistrationDto registrationDto) {
        Registration registration = registrationMapper.toEntity(registrationDto);
        Registration savedRegistration= registrationRepository.save(registration);
        return registrationMapper.toRegistrationDto(savedRegistration);
    }

    public RegistrationDto updateRegistration(RegistrationDto registrationDto) {
        Registration registration = registrationMapper.toEntity(registrationDto);
        Registration savedRegistration= registrationRepository.save(registration);
        return registrationMapper.toRegistrationDto(savedRegistration);
    }

    public RegistrationDto getRegistrationById(Integer id) {
        Registration registration =registrationRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Registration with id " + id + " does not exist."));
        return registrationMapper.toRegistrationDto(registration);
    }

    public List<RegistrationDto> getAllRegistrations() {
        return registrationRepository.findAll().stream().map(registrationMapper::toRegistrationDto).toList();
    }

    public void deleteRegistration(Integer id) {
        Registration registration = registrationRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Registration with id " + id + " does not exist."));
        registrationRepository.deleteById(id);
    }

    public List<RegistrationDto> findRegistrationsByDate(String date) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        return registrationMapper.toRegistrationDtoList(registrationRepository.findRegistrationsByDate(date, username, Sort.by(orders))); //date,
    }

    public List<RegistrationDto> findRegistrationsByWeek(Integer year, Integer week ) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        String firstDayOfWeek = DateUtility.getFirstDayOfWeek(year, week);
        String lastDayOfWeek = DateUtility.getLastDayOfWeek(year, week);
        return registrationMapper.toRegistrationDtoList(registrationRepository.findRegistrationsByDateRange(firstDayOfWeek,lastDayOfWeek, username, Sort.by(orders)));
    }

    public List<RegistrationDto> findRegistrationsByMonth(Integer year, Integer month ) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        String firstDayOfWeek = DateUtility.getFirstDayOfMonth(year, month);
        String lastDayOfWeek = DateUtility.getLastDayOfMonth(year, month);
        return registrationMapper.toRegistrationDtoList(registrationRepository.findRegistrationsByDateRange(firstDayOfWeek,lastDayOfWeek, username, Sort.by(orders)));
    }

    public List<RegistrationDto> findRegistrationsByYear(Integer year) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        String firstDayOfYear = DateUtility.getFirstDayOfYear(year);
        String lastDayOfYear = DateUtility.getLastDayOfYear(year);
        return registrationMapper.toRegistrationDtoList(registrationRepository.findRegistrationsByDateRange(firstDayOfYear, lastDayOfYear, username, Sort.by(orders)));
    }


    public List<ExpenseSummary> findSumRegistrationsByDate(String date) {
        String username = UserUtility.getCurrentUsername();
        return registrationRepository.findSumRegistrationsByDate(date, username); //date,
    }

    public List<ExpenseSummary> findSumRegistrationsByWeek(Integer year, Integer week) {
        String username = UserUtility.getCurrentUsername();
        String firstDayOfWeek = DateUtility.getFirstDayOfWeek(year, week);
        String lastDayOfWeek = DateUtility.getLastDayOfWeek(year, week);
        return registrationRepository.findSumRegistrationsByDateRange(firstDayOfWeek, lastDayOfWeek, username);
    }

    public List<ExpenseSummary> findSumRegistrationsByMonth(Integer year, Integer month) {
        String username = UserUtility.getCurrentUsername();
        String firstDayOfMonth = DateUtility.getFirstDayOfMonth(year, month);
        String lastDayOfMonth = DateUtility.getLastDayOfMonth(year, month);
        return registrationRepository.findSumRegistrationsByDateRange(firstDayOfMonth, lastDayOfMonth, username);
    }

    public List<ExpenseSummary> findSumRegistrationsByYear(Integer year) {
        String username = UserUtility.getCurrentUsername();
        String firstDayOfYear = DateUtility.getFirstDayOfYear(year);
        String lastDayOfYear = DateUtility.getLastDayOfYear(year);
        return registrationRepository.findSumRegistrationsByDateRange(firstDayOfYear, lastDayOfYear, username);
    }

    public List<Sort.Order> getSortOrders() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "expense"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "createdDate"));
        return orders;
    }





}
