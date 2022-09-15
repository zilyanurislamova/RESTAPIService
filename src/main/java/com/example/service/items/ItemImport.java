package com.example.service.items;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ItemImport implements Comparable<ItemImport>{

    private String id;

    private String url;

    private String parentId;

    private String type;

    private Integer size;

    public ItemImport(String id, String url, String parentId, Type type, Integer size) {

        ResponseStatusException BAD_REQUEST = new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (id == null)
            throw BAD_REQUEST;
        this.id = id;

        if (type == null)
            throw BAD_REQUEST;
        this.type = type.getValue();

        if (type == Type.FILE && url.length() > 255 || type == Type.FOLDER && url != null)
            throw BAD_REQUEST;
        this.url = url;

        this.parentId = parentId;

        if (type == Type.FILE && size <= 0 || type == Type.FOLDER && size != null)
            throw BAD_REQUEST;
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

    public Integer getSize() {
        return size;
    }

    @Override
    public int compareTo(ItemImport other) {
        if (this.isFolder() && other.isFile())
            return -1;
        if (this.isFolder() && other.isFolder() && this.parentId == null && other.parentId != null)
            return -1;
        if (this.isFolder() && other.isFolder() && this.id.equals(other.parentId)) {
            return -1;
        }
        return 0;
    }

    public boolean isFolder() {
        return Type.valueOf(this.type) == Type.FOLDER;
    }

    public boolean isFile() {
        return Type.valueOf(this.type) == Type.FILE;
    }
}
