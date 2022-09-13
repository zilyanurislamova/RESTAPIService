package com.example.service.items;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
public class ItemConfiguration {

    private final String parentId = UUID.randomUUID().toString();

    @Bean
    public CommandLineRunner commandLineRunner(ItemRepository itemRepository) {
        return args -> {
            itemRepository.saveAll(List.of(
                    new Item(parentId, LocalDateTime.now().toString(), Type.FOLDER),
                    new Item(UUID.randomUUID().toString(), LocalDateTime.now().toString(), parentId, Type.FILE)
            ));
        };
    }
}
