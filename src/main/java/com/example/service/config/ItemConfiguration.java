package com.example.service.config;

import com.example.service.items.Item;
import com.example.service.items.ItemRepository;
import com.example.service.items.Type;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class ItemConfiguration {

    @Bean
    public CommandLineRunner commandLineRunner(ItemRepository itemRepository) {
        return args -> {
            itemRepository.saveAll(List.of(
                    new Item("folder_1", LocalDateTime.now().toString(), Type.FOLDER, 54),
                    new Item("file_1", LocalDateTime.now().toString(), "folder_1", Type.FILE, 54),
                    new Item("folder_2", LocalDateTime.now().toString(), Type.FOLDER, 0)
            ));
        };
    }
}
