package com.example.garden.controller;

import com.example.garden.model.UnitOfQuantity;
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
    public UnitOfQuantity create(@RequestBody UnitOfQuantity unitOfQuantity){
        return unitOfQuantityService.createUnitOfQuantity(unitOfQuantity);
    }


    @PostMapping("/updateUnitOfQuantity")
    public UnitOfQuantity update(@RequestBody UnitOfQuantity unitOfQuantity) {
        return unitOfQuantityService.updateUnitOfQuantity(unitOfQuantity);

    }

    @DeleteMapping("/deleteUnitOfQuantity/{id}")
    public void delete(@PathVariable Integer id) {
        unitOfQuantityService.deleteUnitOfQuantity(id);
    }

    @GetMapping("/findUnitOfQuantityById/{id}")
    public UnitOfQuantity findById(@PathVariable Integer id) {
        return unitOfQuantityService.findById(id);
    }

    @GetMapping("/findAllUnitOfQuantities")
    public List<UnitOfQuantity> findAll() {
        return unitOfQuantityService.findAll();
    }

}
