package com.example.garden.mapper;

import com.example.garden.dto.UnitOfQuantityDto;
import com.example.garden.model.UnitOfQuantity;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class UnitOfQuantityMapper {

    public UnitOfQuantity toEntity(UnitOfQuantityDto unitOfQuantityDto) {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity();
        unitOfQuantity.setId(unitOfQuantityDto.id());
        unitOfQuantity.setName(unitOfQuantityDto.name());
        return unitOfQuantity;
    }

    public UnitOfQuantityDto toUnitOfQuantityDto(UnitOfQuantity unitOfQuantity) {
        Integer id = unitOfQuantity.getId();
        String name = unitOfQuantity.getName();
        return new UnitOfQuantityDto(id, name);
    }

    public static List<UnitOfQuantityDto> toUnitOfQuantityDtoList(List<UnitOfQuantity> unitOfQuantities) {
        return unitOfQuantities.stream()
                .map(unitOfQuantity -> new UnitOfQuantityDto(unitOfQuantity.getId(), unitOfQuantity.getName()))
                .collect(Collectors.toList());
    }
}

