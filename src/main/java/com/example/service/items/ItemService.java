package com.example.service.items;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void addOrUpdate(List<Item> items) {
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
