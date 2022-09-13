package com.example.service.items;

import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(path = "imports")
    public void importItems(@RequestBody ItemImportRequest itemImportRequest) {
        itemService.importItems(itemImportRequest);
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
