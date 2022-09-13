package com.example.service.items;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Item {

    @Id
    @Column(nullable = false)
    private String id;

    @Column
    private String url;

    @Column(nullable = false)
    private String date;

    @Column
    private String parentId;

    @Column(nullable = false)
    private String type;

    @Column
    private int size;

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Item> children;

    public Item() {
    }

    public Item(String id, String date, Type type) {
        this.id = id;
        this.date = date;
        this.type = type.getValue();
    }

    public Item(String id, String date, String parentId, Type type) {
        this.id = id;
        this.date = date;
        this.parentId = parentId;
        this.type = type.getValue();
    }

    public Item(String id, String url, String date, String parentId, Type type, int size, List<Item> children) {
        this.id = id;
        this.url = url;
        this.date = date;
        this.parentId = parentId;
        this.type = type.getValue();
        this.size = size;
        this.children = children;
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

    public int getSize() {
        return size;
    }

    public List<Item> getChildren() {
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

    public void setType(String type) {
        if (this.type == null)
            this.type = type;
        else if (!this.type.equals(type)) {
            throw new RuntimeException("Validation Failed");
        }
    }

    public void setSize(int size) {
        this.size = size;
    }
}
