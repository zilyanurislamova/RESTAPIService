package com.example.service.items;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final ResponseStatusException BAD_REQUEST = new ResponseStatusException(HttpStatus.BAD_REQUEST);
    private final ResponseStatusException NOT_FOUND = new ResponseStatusException(HttpStatus.NOT_FOUND);

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void importItems(ItemImportRequest itemImportRequest) {
        List<Item> items = new ArrayList<>();
        List<ItemImport> itemImports = itemImportRequest.getItems();
        String updateDate = itemImportRequest.getUpdateDate();
        for (ItemImport itemImport : itemImports) {

            Item item;

            // поле id не может быть равно null
            String id = itemImport.getId();
            if (id == null)
                throw BAD_REQUEST;
            else if (itemRepository.existsById(id))
                item = itemRepository.getReferenceById(id);
            else {
                item = new Item();
                item.setId(id);
            }

            // при обновлении параметров элемента обязательно обновляется поле date в соответствии с временем обновления
            // дата обрабатывается согласно ISO 8601 (такой придерживается OpenAPI).
            // Если дата не удовлетворяет данному формату, ответом будет код 400
            try {
                LocalDateTime.parse(updateDate, DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeParseException e) {
                throw BAD_REQUEST;
            }
            item.setDate(updateDate);

            // Изменение типа элемента с папки на файл и с файла на папку не допускается
            String stringType = itemImport.getType();
            Type type = Type.valueOf(stringType);
            if (item.getType() == null)
                item.setType(type);
            else if (!item.getType().equals(stringType))
                throw BAD_REQUEST;

            // поле url при импорте папки всегда должно быть равно null
            // размер поля url при импорте файла всегда должен быть меньше либо равным 255
            String url = itemImport.getUrl();
            if ((item.isFolder() && url == null)
                    || (item.isFile() && url != null && url.length() <= 255))
                item.setUrl(url);
            else
                throw BAD_REQUEST;

            // родителем элемента может быть только папка
            // принадлежность к папке определяется полем parentId
            // элементы могут не иметь родителя (при обновлении parentId на null элемент остается без родителя)
            String parentId = itemImport.getParentId();
            if (parentId == null || itemRepository.existsById(parentId) && itemRepository.getReferenceById(parentId).isFolder()) {
                item.setParentId(parentId);
            }
            else
                throw BAD_REQUEST;

            // поле size для файлов всегда должно быть больше 0
            // поле size при импорте папки всегда должно быть равно null
            Integer size = itemImport.getSize();
            if ((item.isFile() && size > 0) || (item.isFolder() && size == null))
                item.setSize(size);
            else
                throw BAD_REQUEST;

            // в одном запросе не может быть двух элементов с одинаковым id
            if (!items.contains(item))
                items.add(item);
            else
                throw BAD_REQUEST;
        }
        itemRepository.saveAll(items);
    }

    public void deleteById(String id, String date) {
        try {
            LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw BAD_REQUEST;
        }

        if (itemRepository.existsById(id)) {
            Item toBeDeleted = itemRepository.getReferenceById(id);
            List<Item> children = toBeDeleted.getChildren();
            itemRepository.deleteAll(children);
            itemRepository.deleteById(id);
        } else
            throw NOT_FOUND;
    }

    public Item getInfoById(String id) {
        if (itemRepository.existsById(id))
            return itemRepository.getReferenceById(id);
        else
            throw NOT_FOUND;
    }
}
