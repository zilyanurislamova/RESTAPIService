package com.example.service.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "file", "folder"})
public class Item{

    @Id
    @Column(nullable = false)
    private String id;

    @Column()
    private String url;

    @Column(nullable = false)
    private String date;

    @Column
    private String parentId;

    @Column(nullable = false)
    private String type;

    @Column
    private Integer size;

    @OneToMany(mappedBy = "parentId")
    private List<Item> children;

    public Item() {
    }

    public Item(String id, String date, Type type, Integer size) {
        this.id = id;
        this.date = date;
        this.type = type.getValue();
        this.size = size;
    }

    public Item(String id, String date, String parentId, Type type, Integer size) {
        this.id = id;
        this.date = date;
        this.parentId = parentId;
        this.type = type.getValue();
        this.size = size;
    }

    public Item(String id, String url, String date, String parentId, Type type, Integer size) {
        this.id = id;
        this.url = url;
        this.date = date;
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

    public String getDate() {
        return date;
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

    public List<Item> getChildren() {
        if (this.isFile())
            return null;
        return children;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setType(Type type) {
        this.type = type.getValue();
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isFolder() {
        return Type.valueOf(this.type) == Type.FOLDER;
    }

    public boolean isFile() {
        return Type.valueOf(this.type) == Type.FILE;
    }
}
