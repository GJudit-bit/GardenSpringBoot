package com.example.garden.controller;

import com.example.garden.dto.RegistrationDto;
import com.example.garden.model.ExpenseSummary;
import com.example.garden.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garden")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/addRegistration")
    public RegistrationDto addRegistration(@RequestBody RegistrationDto registrationDto) {
        return registrationService.createRegistration(registrationDto);
    }

    @PostMapping("/updateRegistration")
    public RegistrationDto updateRegistration(@RequestBody RegistrationDto registrationDto) {
        return registrationService.updateRegistration(registrationDto);
    }

    @GetMapping("/getRegistrationById/{id}")
    public RegistrationDto getRegistrationById(@PathVariable Integer id) {
        return registrationService.getRegistrationById(id);
    }

    @GetMapping("/getAllRegistrations")
    public Iterable<RegistrationDto> getAllRegistrations() {
        List<RegistrationDto> list= registrationService.getAllRegistrations();
        return list;
    }

    @DeleteMapping("/deleteRegistration/{id}")
    public void deleteRegistration(@PathVariable Integer id) {
        registrationService.deleteRegistration(id);
    }

    @GetMapping("/findRegistrationsByDate/{date}")
    public List<RegistrationDto> findRegistrationsByDate(@PathVariable("date") String date) {
        return registrationService.findRegistrationsByDate(date);
    }

    @GetMapping("/findRegistrationsByWeek/{year}/{week}")
    public List<RegistrationDto> findRegistrationsByWeek(@PathVariable("year") Integer year, @PathVariable("week") Integer week) {
        return registrationService.findRegistrationsByWeek(year, week);
    }

    @GetMapping("/findRegistrationsByMonth/{year}/{month}")
    public List<RegistrationDto> findRegistrationsByMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
        return registrationService.findRegistrationsByMonth(year, month);
    }

    @GetMapping("/findRegistrationsByYear/{year}")
    public List<RegistrationDto> findRegistrationsByYear(@PathVariable("year") Integer year) {
        return registrationService.findRegistrationsByYear(year);
    }

    @GetMapping("/findSumRegistrationsByDate/{date}")
    public List<ExpenseSummary> findSumRegistrationsByDate(@PathVariable("date") String date) {
        return registrationService.findSumRegistrationsByDate(date);
    }

    @GetMapping("/findSumRegistrationsByWeek/{year}/{week}")
    public List<ExpenseSummary> findSumRegistrationsByWeek(@PathVariable("year") Integer year, @PathVariable("week") Integer week) {
        return registrationService.findSumRegistrationsByWeek(year, week);
    }

    @GetMapping("/findSumRegistrationsByMonth/{year}/{month}")
    public List<ExpenseSummary> findSumRegistrationsByMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
        return registrationService.findSumRegistrationsByMonth(year, month);
    }

    @GetMapping("/findSumRegistrationsByYear/{year}")
    public List<ExpenseSummary> findSumRegistrationsByYear(@PathVariable("year") Integer year) {
        return registrationService.findSumRegistrationsByYear(year);
    }

}
