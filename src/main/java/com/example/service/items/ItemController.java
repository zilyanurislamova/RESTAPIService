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

    @DeleteMapping(path = "delete/{id}", params = "date")
    public void deleteById(@PathVariable String id, @RequestParam("date") String date) {
        itemService.deleteById(id, date);
    }

    @GetMapping(path = "nodes/{id}")
    public Item getInfoById(@PathVariable String id) {
        return itemService.getInfoById(id);
    }
}
