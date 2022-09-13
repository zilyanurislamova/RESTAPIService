package com.example.service.items;

import org.springframework.stereotype.Service;

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
            item.setUrl(itemImport.getUrl());
            item.setParentId(itemImport.getParentId());
            item.setType(itemImport.getType());
            item.setSize(itemImport.getSize());
            items.add(item);
        }
        itemRepository.saveAll(items);
    }

    public void deleteById(String id) {
        Item toBeDeleted = itemRepository.getReferenceById(id);
        List<Item> children = toBeDeleted.getChildren();
        itemRepository.deleteAll(children);
        itemRepository.deleteById(id);
    }

    public Item getInfoById(String id) {
        return itemRepository.getReferenceById(id);
    }
}
