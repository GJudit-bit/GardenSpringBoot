package com.example.garden.service;


import com.example.garden.dto.ItemDto;
import com.example.garden.mapper.ItemMapper;
import com.example.garden.model.Item;
import com.example.garden.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto createItem(ItemDto itemDto) {
        Item item=itemMapper.toEntity(itemDto);
        Item savedItem= itemRepository.save(item);
        return itemMapper.toItemDto(savedItem);
    }


    public ItemDto findById(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Item with id " + id + " does not exist."));
        return itemMapper.toItemDto(item);
    }

    public ItemDto updateItem(ItemDto itemDto) {
        Item item=itemMapper.toEntity(itemDto);
        Item savedItem= itemRepository.save(item);
        return itemMapper.toItemDto(savedItem);
    }

    public void deleteItem(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Item with id " + id + " does not exist."));
        itemRepository.deleteById(item.getId());
    }

    public List<ItemDto> getAllItems(Sort sort) {
        return itemRepository.findAll().stream().map(itemMapper::toItemDto).toList();
    }


    public List<ItemDto> getItemsByUnitOfQuantity(Integer id) {
        return itemRepository.findByUnitOfQuantity_Id(id).stream().map(itemMapper::toItemDto).toList();
    }

    public List<ItemDto> getItemsByCategory(Integer id) {
        return itemRepository.findByCategory_Id(id).stream().map(itemMapper::toItemDto).toList();
    }
}
