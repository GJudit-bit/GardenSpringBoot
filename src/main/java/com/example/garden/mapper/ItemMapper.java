package com.example.garden.mapper;

import com.example.garden.dto.ItemDto;
import com.example.garden.model.Category;
import com.example.garden.model.Item;
import com.example.garden.model.UnitOfQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ItemMapper {

    private final CategoryMapper categoryMapper;
    private final UnitOfQuantityMapper unitOfQuantityMapper;

    @Autowired
    public ItemMapper(CategoryMapper categoryMapper, UnitOfQuantityMapper unitOfQuantityMapper) {
        this.categoryMapper = categoryMapper;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
    }

    public  Item toEntity(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.id());
        item.setName(itemDto.name());
        item.setUnitOfQuantity(itemDto.unitOfQuantityDto()==null? null:unitOfQuantityMapper.toEntity(itemDto.unitOfQuantityDto()));
        item.addCategory(itemDto.categoryDto()==null ? null:categoryMapper.toEntity(itemDto.categoryDto()));
        return item;
    }

    public ItemDto toItemDto(Item item) {
        Integer id = item.getId();
        String name = item.getName();
        Category category = item.getCategory();
        UnitOfQuantity unitOfQuantity = item.getUnitOfQuantity();
        return new ItemDto(id, name, category==null?null:categoryMapper.toCategoryDto(category), unitOfQuantity==null?null:unitOfQuantityMapper.toUnitOfQuantityDto(unitOfQuantity));
    }

    public List<ItemDto> toItemDtoList(List<Item> items) {
        return items.stream().map(item -> {
            Integer id = item.getId();
            String name = item.getName();
            Category category = item.getCategory();
            UnitOfQuantity unitOfQuantity = item.getUnitOfQuantity();
            return new ItemDto(id, name, category==null? null:categoryMapper.toCategoryDto(category), unitOfQuantity==null? null:unitOfQuantityMapper.toUnitOfQuantityDto(unitOfQuantity));
        }).toList();
    }
}
