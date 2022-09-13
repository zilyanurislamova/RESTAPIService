package com.example.service.items;

import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/imports")
    public void addOrUpdate() {

    }

    @DeleteMapping("/delete/{id}")
    public void delete() {

    }

    @GetMapping("/nodes/{id}")
    public void get() {

    }
}
