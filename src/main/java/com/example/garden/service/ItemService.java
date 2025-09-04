package com.example.garden.service;


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

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }


    public Item getItemById(Integer id) {
       return itemRepository.findById(id).orElse(null);
    }

    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Item with id " + id + " does not exist."));

        itemRepository.deleteById(item.getId());
    }

    public List<Item> getAllItems(Sort sort) {
        return itemRepository.findAll();
    }


    public List<Item> getItemsByUnitOfQuantity(Integer id) {
        return itemRepository.findByUnitOfQuantity_Id(id);
    }

    public List<Item> getItemsByCategory(Integer id) {

        return itemRepository.findByCategory_Id(id);
    }


}
