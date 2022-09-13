package com.example.service.items;

import java.util.List;

public class ItemImportRequest {

    private List<ItemImport> items;

    private String updateDate;

    public ItemImportRequest(List<ItemImport> items, String updateDate) {
        this.items = items;
        this.updateDate = updateDate;
    }

    public List<ItemImport> getItems() {
        return items;
    }

    public String getUpdateDate() {
        return updateDate;
    }
}
