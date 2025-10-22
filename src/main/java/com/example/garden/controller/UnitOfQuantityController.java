package com.example.garden.controller;

import com.example.garden.dto.UnitOfQuantityDto;
import com.example.garden.service.UnitOfQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garden")
public class UnitOfQuantityController {

    private final UnitOfQuantityService unitOfQuantityService;

    @Autowired
    public UnitOfQuantityController(UnitOfQuantityService unitOfQuantityService) {
        this.unitOfQuantityService = unitOfQuantityService;
    }

    @PostMapping("/addUnitOfQuantity")
    public UnitOfQuantityDto create(@RequestBody UnitOfQuantityDto unitOfQuantityDto){
        return unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
    }


    @PostMapping("/updateUnitOfQuantity")
    public UnitOfQuantityDto update(@RequestBody UnitOfQuantityDto unitOfQuantityDto) {
        return unitOfQuantityService.updateUnitOfQuantity(unitOfQuantityDto);
    }

    @DeleteMapping("/deleteUnitOfQuantity/{id}")
    public void delete(@PathVariable Integer id) {
        unitOfQuantityService.deleteUnitOfQuantity(id);
    }

    @GetMapping("/findUnitOfQuantityById/{id}")
    public UnitOfQuantityDto findById(@PathVariable Integer id) {
        return unitOfQuantityService.findById(id);
    }

    @GetMapping("/findAllUnitOfQuantities")
    public List<UnitOfQuantityDto> findAll() {
        return unitOfQuantityService.findAll();
    }
}
