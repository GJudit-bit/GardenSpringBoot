package com.example.garden.service;

import com.example.garden.model.Registration;
import com.example.garden.model.ExpenseSummary;
import com.example.garden.repository.RegistrationRepository;

import com.example.garden.utility.DateUtility;
import com.example.garden.utility.UserUtility;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }


    public Registration createRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }

    public Registration updateRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }

    public Registration getRegistrationById(Integer id) {
        return registrationRepository.findById(id).orElse(null);
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public void deleteRegistration(Integer id) {
        Registration registration = registrationRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Item with id " + id + " does not exist."));
        registrationRepository.deleteById(id);
    }

    public List<Registration> findRegistrationsByDate(String date) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        return registrationRepository.findRegistrationsByDate(date, username, Sort.by(orders)); //date,
    }

    public List<Registration> findRegistrationsByWeek(Integer year, Integer week ) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        String firstDayOfWeek = DateUtility.getFirstDayOfWeek(year, week);
        String lastDayOfWeek = DateUtility.getLastDayOfWeek(year, week);
        return registrationRepository.findRegistrationsByDateRange(firstDayOfWeek,lastDayOfWeek, username, Sort.by(orders));
    }

    public List<Registration> findRegistrationsByMonth(Integer year, Integer month ) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        String firstDayOfWeek = DateUtility.getFirstDayOfMonth(year, month);
        String lastDayOfWeek = DateUtility.getLastDayOfMonth(year, month);
        return registrationRepository.findRegistrationsByDateRange(firstDayOfWeek,lastDayOfWeek, username, Sort.by(orders));
    }

    public List<Registration> findRegistrationsByYear(Integer year) {
        List<Sort.Order> orders = getSortOrders();
        String username = UserUtility.getCurrentUsername();
        String firstDayOfYear = DateUtility.getFirstDayOfYear(year);
        String lastDayOfYear = DateUtility.getLastDayOfYear(year);
        return registrationRepository.findRegistrationsByDateRange(firstDayOfYear, lastDayOfYear, username, Sort.by(orders));
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
