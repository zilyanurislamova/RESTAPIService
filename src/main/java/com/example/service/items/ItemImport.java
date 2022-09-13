package com.example.service.items;

public class ItemImport {

    private String id;

    private String url;

    private String parentId;

    private String type;

    private int size;

    public ItemImport(String id, String url, String parentId, Type type, int size) {
        this.id = id;
        this.url = url;
        this.parentId = parentId;
        this.type = type.getValue();
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getParentId() {
        return parentId;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
