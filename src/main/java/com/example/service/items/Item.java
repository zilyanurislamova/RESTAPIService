package com.example.service.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Override
    public boolean equals(Object object) {
        Item other;
        if (object instanceof Item)
            other = (Item)object;
        else
            return false;
        return this.id.equals(other.id);
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

    public void setType(Type type) {
        this.type = type.getValue();
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isFolder() {
        return this.type.equals(Type.FOLDER.getValue());
    }

    public boolean isFile() {
        return this.type.equals(Type.FILE.getValue());
    }
}
