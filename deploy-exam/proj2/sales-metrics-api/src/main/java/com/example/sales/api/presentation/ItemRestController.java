package com.example.sales.api.presentation;

import com.example.sales.api.application.service.ItemApplicationService;
import com.example.sales.api.domain.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemRestController {
    private final ItemApplicationService itemApplicationService;

    public ItemRestController(ItemApplicationService itemApplicationService) {
        this.itemApplicationService = itemApplicationService;
    }

    @GetMapping
    public Page<Item> getItem(Pageable pageable) {
        return itemApplicationService.listItem(pageable);
    }
}
