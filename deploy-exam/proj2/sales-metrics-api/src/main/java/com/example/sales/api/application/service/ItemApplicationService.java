package com.example.sales.api.application.service;

import com.example.sales.api.domain.entity.Item;
import com.example.sales.api.domain.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemApplicationService {
    private final ItemRepository itemRepository;

    public ItemApplicationService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> listItem(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }
}
