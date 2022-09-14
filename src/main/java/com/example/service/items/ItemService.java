package com.example.service.items;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void importItems (ItemImportRequest itemImportRequest) {
        List<Item> items = new ArrayList<>();
        List<ItemImport> itemImports = itemImportRequest.getItems();
        String updateDate = itemImportRequest.getUpdateDate();
        for (ItemImport itemImport: itemImports) {
            String id = itemImport.getId();
            Item item;
            if (itemRepository.existsById(id))
                item = itemRepository.getReferenceById(id);
            else
                item = new Item();
            item.setId(id);
            item.setDate(updateDate);
            item.setType(itemImport.getType());
            item.setUrl(itemImport.getUrl());
            item.setParentId(itemImport.getParentId());
            item.setSize(itemImport.getSize());
            if (!items.contains(item))
                items.add(item);
            else
                throw new RuntimeException("Validation Failed");
        }
        itemRepository.saveAll(items);
    }

    public void deleteById(String id, String date) {
        try {
            LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        }
        catch (DateTimeParseException e) {
            throw new RuntimeException("Validation Failed");
        }

        if (itemRepository.existsById(id)) {
            Item toBeDeleted = itemRepository.getReferenceById(id);
            List<Item> children = toBeDeleted.getChildren();
            itemRepository.deleteAll(children);
            itemRepository.deleteById(id);
        }
        else
            throw new RuntimeException("Item not found");
    }

    public Item getInfoById(String id) {
        if (itemRepository.existsById(id))
            return itemRepository.getReferenceById(id);
        else
            throw new RuntimeException("Item not found");
    }
}
