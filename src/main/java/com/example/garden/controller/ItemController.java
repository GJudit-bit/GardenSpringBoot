package com.example.garden.controller;

import com.example.garden.model.Item;
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
        public Item create(@RequestBody Item item){
        return itemService.createItem(item);
    }


        @PostMapping("/updateItem")
        public Item update(@RequestBody Item item) {
        return itemService.updateItem(item);

    }

        @DeleteMapping("/deleteItem/{id}")
        public void delete(@PathVariable Integer id) {
            itemService.deleteItem(id);
    }

        @GetMapping("/findItemById/{id}")
        public Item findById(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

        @GetMapping("/findAllItem")
        public List<Item> findAll() {
        return itemService.getAllItems(Sort.by("name"));
    }

    @GetMapping("/findItemsByUnitOfQuantityId/{id}")
    public List<Item> findByItemId(@PathVariable Integer itemId) {
        return itemService.getItemsByUnitOfQuantity(itemId);
    }
    }

