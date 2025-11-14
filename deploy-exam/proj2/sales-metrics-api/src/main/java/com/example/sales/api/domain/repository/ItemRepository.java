package com.example.sales.api.domain.repository;

import com.example.sales.api.domain.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemRepository {
    Page<Item> findAll(Pageable pageable);
    Optional<Item> findById(Integer id);
}
