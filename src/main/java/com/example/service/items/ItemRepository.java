package com.example.service.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;


public interface ItemRepository extends JpaRepository<Item, String> {

    @Query( "SELECT item.id FROM Item item WHERE item.type = 'FOLDER'")
    Set<String> findAllFoldersIds();

    @Query( "SELECT item.type FROM Item item WHERE item.id = ?1")
    String getTypeById(String id);
}
