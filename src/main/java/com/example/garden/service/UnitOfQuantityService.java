package com.example.garden.service;


import com.example.garden.dto.UnitOfQuantityDto;
import com.example.garden.mapper.UnitOfQuantityMapper;
import com.example.garden.model.Item;
import com.example.garden.model.UnitOfQuantity;
import com.example.garden.repository.ItemRepository;
import com.example.garden.repository.UnitOfQuantityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitOfQuantityService {

    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final ItemRepository itemRepository;
    private final UnitOfQuantityMapper unitOfQuantityMapper;

    @Autowired
    public UnitOfQuantityService(UnitOfQuantityRepository unitOfQuantityRepository, ItemRepository itemRepository, UnitOfQuantityMapper unitOfQuantityMapper) {
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.itemRepository = itemRepository;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public UnitOfQuantityDto createUnitOfQuantity(UnitOfQuantityDto unitOfQuantityDto) {
        UnitOfQuantity savedUnitOfQuantity=  unitOfQuantityRepository.save(unitOfQuantityMapper.toEntity(unitOfQuantityDto));
        return unitOfQuantityMapper.toUnitOfQuantityDto(savedUnitOfQuantity);
    }

    public UnitOfQuantityDto updateUnitOfQuantity(UnitOfQuantityDto unitOfQuantityDto) {
        UnitOfQuantity savedUnitOfQuantity=  unitOfQuantityRepository.save(unitOfQuantityMapper.toEntity(unitOfQuantityDto));
        return unitOfQuantityMapper.toUnitOfQuantityDto(savedUnitOfQuantity);
    }


    public void deleteUnitOfQuantity(Integer id) {
        UnitOfQuantity unitOfQuantity = unitOfQuantityRepository.findById(id).orElse(null);

        if (unitOfQuantity != null) {
            List<Item> items = itemRepository.findByUnitOfQuantity_Id(unitOfQuantity.getId());

            if (items.isEmpty()) {
                unitOfQuantityRepository.deleteById(id);
            } else {
                throw new IllegalStateException("Cannot delete UnitOfQuantity with id " + id + " because it is associated with items.");
            }
        } else {
            throw new EntityNotFoundException("UnitOfQuantity with id " + id + " does not exist.");
        }

    }

    public UnitOfQuantityDto findById(Integer id) {
        UnitOfQuantity  unitOfQuantity=unitOfQuantityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("UnitOfQuantity with id " + id + " does not exist."));
        return unitOfQuantityMapper.toUnitOfQuantityDto(unitOfQuantity);
    }

    public List<UnitOfQuantityDto> findAll() {
        return unitOfQuantityRepository.findAll().stream()
                .map(unitOfQuantityMapper::toUnitOfQuantityDto)
                .toList();
    }
}
