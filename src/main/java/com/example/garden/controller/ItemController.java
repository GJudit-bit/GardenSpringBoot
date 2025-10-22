package com.example.garden.controller;

import com.example.garden.dto.ItemDto;
import com.example.garden.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RestController
@EnableWebMvc
@RequestMapping("/garden")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/addItem")
    public ItemDto create(@RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto);
    }


    @PostMapping("/updateItem")
    public ItemDto update(@RequestBody ItemDto itemDto) {
        return itemService.updateItem(itemDto);
    }

    @DeleteMapping("/deleteItem/{id}")
    public void delete(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/findItemById/{id}")
    public ItemDto findById(@PathVariable Integer id) {
        return itemService.findById(id);
    }

    @GetMapping("/findAllItem")
    public List<ItemDto> findAll() {
        return itemService.getAllItems(Sort.by("name"));
    }


    @GetMapping("/findItemsByUnitOfQuantityId/{id}")
    public List<ItemDto> findByItemId(@PathVariable Integer itemId) {
        return itemService.getItemsByUnitOfQuantity(itemId);
    }
}

