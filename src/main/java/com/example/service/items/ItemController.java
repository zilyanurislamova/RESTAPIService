package com.example.service.items;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(path = "imports")
    public void addOrUpdate(@RequestBody List<Item> items) {
        itemService.addOrUpdate(items);
    }

    @DeleteMapping(path = "delete/{id}")
    public void deleteById(@PathVariable String id) {
        itemService.deleteById(id);
    }

    @GetMapping(path = "nodes/{id}")
    public Item getInfoById(@PathVariable String id) {
        return itemService.getInfoById(id);
    }
}
