package com.example.service.items;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service

public class ItemService {

    private final ItemRepository itemRepository;

    private final ResponseStatusException BAD_REQUEST = new ResponseStatusException(HttpStatus.BAD_REQUEST);

    private final ResponseStatusException NOT_FOUND = new ResponseStatusException(HttpStatus.NOT_FOUND);

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    private boolean requestIsValid(ItemImportRequest itemImportRequest) {
        List<ItemImport> itemImports = itemImportRequest.getItems();
        String updateDate = itemImportRequest.getUpdateDate();

        try {
            LocalDateTime.parse(updateDate, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            return false;
        }

        Collections.sort(itemImports);

        Set<String> parentIds;
        if (itemRepository.findAllFoldersIds() != null)
            parentIds = itemRepository.findAllFoldersIds();
        else
            parentIds = new HashSet<>();

        Set<String> ids = new HashSet<>();
        for (ItemImport itemImport: itemImports) {
            String id = itemImport.getId();
            if (ids.contains(id))
                return false;
            else ids.add(id);

            String type = itemImport.getType();
            if (itemRepository.existsById(id) && !itemRepository.getTypeById(id).equals(type))
                return false;

            if (itemImport.isFolder())
                parentIds.add(id);

            String parentId = itemImport.getParentId();
            if (parentId != null && !parentIds.contains(parentId))
                return false;
        }
        return true;
    }

    private void updateSizeAndParent(Item item, ItemImport itemImport) {
        String parentId = itemImport.getParentId();
        String prevParentId = item.getParentId();
        item.setParentId(parentId);

        Integer size = itemImport.getSize();
        Integer prevSize = item.getSize();
        if (prevSize == null)
            prevSize = 0;
        if (size == null)
            size = prevSize;
        item.setSize(size);

        Item parent;
        Integer parentSize;
        Item prevParent;
        Integer prevParentSize;
        while (parentId != null) {
            parent = itemRepository.getReferenceById(parentId);
            parentSize = parent.getSize();
            parent.setSize(parentSize + size);
            parentId = parent.getParentId();
            itemRepository.save(parent);
        }
        while (prevParentId != null) {
            prevParent = itemRepository.getReferenceById(prevParentId);
            prevParentSize = prevParent.getSize();
            prevParent.setSize(prevParentSize - prevSize);
            prevParentId = prevParent.getParentId();
            itemRepository.save(prevParent);
        }
    }

    public void importItems(ItemImportRequest itemImportRequest) {
        if (!requestIsValid(itemImportRequest))
            throw BAD_REQUEST;

        List<ItemImport> itemImports = itemImportRequest.getItems();
        String updateDate = itemImportRequest.getUpdateDate();
        for (ItemImport itemImport : itemImports) {
            Item item;
            String id = itemImport.getId();
            if (itemRepository.existsById(id))
                item = itemRepository.getReferenceById(id);
            else {
                item = new Item();
                item.setId(id);
            }

            item.setDate(updateDate);

            String stringType = itemImport.getType();
            Type type = Type.valueOf(stringType);
            if (item.getType() == null)
                item.setType(type);

            String url = itemImport.getUrl();
            item.setUrl(url);

            updateSizeAndParent(item, itemImport);

            itemRepository.save(item);
        }
    }

    public void deleteById(String id, String date) {
        try {
            LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw BAD_REQUEST;
        }

        if (itemRepository.existsById(id)) {
            Item toBeDeleted = itemRepository.getReferenceById(id);
            String parentId = toBeDeleted.getParentId();
            Integer size = toBeDeleted.getSize();
            Item parent;
            Integer parentSize;
            while (parentId != null) {
                parent = itemRepository.getReferenceById(parentId);
                parentSize = parent.getSize();
                parent.setSize(parentSize - size);
                parentId = parent.getParentId();
                itemRepository.save(parent);
            }
            deleteItemWithChildren(toBeDeleted);
        } else
            throw NOT_FOUND;
    }

    private void deleteItemWithChildren(Item item) {
        List<Item> children = item.getChildren();
        if (children == null || children.size() == 0) {
            itemRepository.delete(item);
            return;
        }
        for (Item child: children) {
            deleteItemWithChildren(child);
        }
        itemRepository.delete(item);
    }

    public Item getInfoById(String id) {
        if (itemRepository.existsById(id))
            return itemRepository.getReferenceById(id);
        else
            throw NOT_FOUND;
    }
}
